package com.comincini_micheli.quest4run.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Equipment;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.List;

/**
 * Created by Daniele on 01/06/2017.
 */

public class EquipmentInventoryAdapter extends BaseAdapter {
    private Activity activity;
    private List<Equipment> data;
    private static LayoutInflater inflater=null;

    private Equipment equipment_actual;
    private int indexEquipmentEquipped;
    private int idCharacter;
    DatabaseHandler db;

    public EquipmentInventoryAdapter(Activity a, List<Equipment> d, DatabaseHandler db) {
        activity = a;
        data=d;
        Log.w("adapter",""+data.size());
        this.db = db;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexEquipmentEquipped = -1;
        SharedPreferences settings = activity.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        idCharacter = settings.getInt(Constants.CHAR_ID_PREFERENCE,-1);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.inventory_list_row, null);

        ImageView icon = (ImageView)vi.findViewById(R.id.inventory_equipment_icon);
        TextView name = (TextView)vi.findViewById(R.id.inventory_equipment_name);
        TextView attack = (TextView)vi.findViewById(R.id.inventory_equipment_attack);
        TextView defense = (TextView)vi.findViewById(R.id.inventory_equipment_defense);
        TextView magic = (TextView)vi.findViewById(R.id.inventory_equipment_magic);
        RadioButton equipped = (RadioButton)vi.findViewById(R.id.radiobutton_equipped);

        equipment_actual = data.get(position);

        String [] icons = activity.getResources().getStringArray(R.array.equipment_icons);
        int id = activity.getResources().getIdentifier(icons[equipment_actual.getIdType()],"drawable", activity.getPackageName());
        icon.setImageResource(id);
        icon.setColorFilter(Color.parseColor(equipment_actual.getIcon()));

        // Setting all values in listview
        name.setText(equipment_actual.getName());
        attack.setText(String.valueOf(equipment_actual.getAtk()));
        defense.setText(String.valueOf(equipment_actual.getDef()));
        magic.setText(String.valueOf(equipment_actual.getMgc()));
        equipped.setChecked(equipment_actual.isEquipped());
        if(equipment_actual.isEquipped())
            indexEquipmentEquipped = position;

        equipped.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    final RadioButton radiobutton_equipped = (RadioButton) view;
                    if (!radiobutton_equipped.isChecked()) {
                        if (indexEquipmentEquipped != -1) {
                            final Equipment previousEquippedEquipment = data.get(indexEquipmentEquipped);
                            equipment_actual = data.get(position);

                            final Quest quest = db.getActiveQuest();
                            if (quest != null) {
                                Character character = db.getCharacter(idCharacter);
                                int charAttackUpdated = character.getAttack() - previousEquippedEquipment.getAtk() + equipment_actual.getAtk();
                                int charDefenseUpdated = character.getDefense() - previousEquippedEquipment.getDef() + equipment_actual.getDef();
                                int charMagicUpdated = character.getMagic() - previousEquippedEquipment.getMgc() + equipment_actual.getMgc();
                                if ((quest.getMinAttack() <= charAttackUpdated) && (quest.getMinDefense() <= charDefenseUpdated) && (quest.getMinMagic() <= charMagicUpdated)) {
                                    previousEquippedEquipment.setEquipped(false);
                                    db.unequipEquipment(previousEquippedEquipment, idCharacter);
                                    data.set(indexEquipmentEquipped, previousEquippedEquipment);

                                    equipment_actual.setEquipped(true);
                                    db.equipEquipment(equipment_actual, idCharacter);
                                    data.set(position, equipment_actual);
                                    indexEquipmentEquipped = position;
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    String request = String.format(activity.getResources().getString(R.string.request_change_quest_unequipping), quest.getTitle());
                                    builder.setMessage(request)
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    previousEquippedEquipment.setEquipped(false);
                                                    db.unequipEquipment(previousEquippedEquipment, idCharacter);
                                                    data.set(indexEquipmentEquipped, previousEquippedEquipment);

                                                    equipment_actual.setEquipped(true);
                                                    db.equipEquipment(equipment_actual, idCharacter);
                                                    data.set(position, equipment_actual);
                                                    indexEquipmentEquipped = position;

                                                    quest.setActive(false);
                                                    db.updateQuest(quest);

                                                    notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    equipment_actual = previousEquippedEquipment;
                                                    notifyDataSetChanged();
                                                }
                                            });
                                    // Create the AlertDialog object and return it
                                    builder.create();
                                    builder.show();
                                }
                            } else {
                                previousEquippedEquipment.setEquipped(false);
                                db.unequipEquipment(previousEquippedEquipment, idCharacter);
                                data.set(indexEquipmentEquipped, previousEquippedEquipment);

                                equipment_actual.setEquipped(true);
                                db.equipEquipment(equipment_actual, idCharacter);
                                data.set(position, equipment_actual);
                                indexEquipmentEquipped = position;
                            }
                        } else {
                            equipment_actual = data.get(position);

                            final Quest quest = db.getActiveQuest();
                            if (quest != null) {
                                Character character = db.getCharacter(idCharacter);
                                int charAttackUpdated = character.getAttack() + equipment_actual.getAtk();
                                int charDefenseUpdated = character.getDefense() + equipment_actual.getDef();
                                int charMagicUpdated = character.getMagic() + equipment_actual.getMgc();
                                if ((quest.getMinAttack() <= charAttackUpdated) && (quest.getMinDefense() <= charDefenseUpdated) && (quest.getMinMagic() <= charMagicUpdated)) {
                                    equipment_actual.setEquipped(true);
                                    db.equipEquipment(equipment_actual, idCharacter);
                                    data.set(position, equipment_actual);
                                    indexEquipmentEquipped = position;
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    String request = String.format(activity.getResources().getString(R.string.request_equip_quest_unequipping), quest.getTitle());
                                    builder.setMessage(request)
                                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    equipment_actual.setEquipped(true);
                                                    db.equipEquipment(equipment_actual, idCharacter);
                                                    data.set(position, equipment_actual);
                                                    indexEquipmentEquipped = position;
                                                    quest.setActive(false);
                                                    db.updateQuest(quest);

                                                    notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    notifyDataSetChanged();
                                                }
                                            });
                                    // Create the AlertDialog object and return it
                                    builder.create();
                                    builder.show();
                                }
                            } else {
                                equipment_actual.setEquipped(true);
                                db.equipEquipment(equipment_actual, idCharacter);
                                data.set(position, equipment_actual);
                                indexEquipmentEquipped = position;
                            }
                        }
                    } else {
                        final Equipment previousEquippedEquipment = data.get(indexEquipmentEquipped);
                        final Quest quest = db.getActiveQuest();
                        if (quest != null) {
                            Character character = db.getCharacter(idCharacter);
                            int charAttackUpdated = character.getAttack() - previousEquippedEquipment.getAtk();
                            int charDefenseUpdated = character.getDefense() - previousEquippedEquipment.getDef();
                            int charMagicUpdated = character.getMagic() - previousEquippedEquipment.getMgc();
                            if ((quest.getMinAttack() <= charAttackUpdated) && (quest.getMinDefense() <= charDefenseUpdated) && (quest.getMinMagic() <= charMagicUpdated)) {
                                previousEquippedEquipment.setEquipped(false);
                                db.unequipEquipment(previousEquippedEquipment, idCharacter);
                                data.set(indexEquipmentEquipped, previousEquippedEquipment);
                                indexEquipmentEquipped = -1;
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                String request = String.format(activity.getResources().getString(R.string.request_remove_quest_unequipping), quest.getTitle());
                                builder.setMessage(request)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                previousEquippedEquipment.setEquipped(false);
                                                db.unequipEquipment(previousEquippedEquipment, idCharacter);
                                                data.set(indexEquipmentEquipped, previousEquippedEquipment);
                                                indexEquipmentEquipped = -1;
                                                quest.setActive(false);
                                                db.updateQuest(quest);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                notifyDataSetChanged();
                                            }
                                        });
                                // Create the AlertDialog object and return it
                                builder.create();
                                builder.show();
                            }
                        } else {
                            previousEquippedEquipment.setEquipped(false);
                            db.unequipEquipment(previousEquippedEquipment, idCharacter);
                            data.set(indexEquipmentEquipped, previousEquippedEquipment);
                            indexEquipmentEquipped = -1;
                        }
                    }
                    notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });

        return vi;
    }
}
