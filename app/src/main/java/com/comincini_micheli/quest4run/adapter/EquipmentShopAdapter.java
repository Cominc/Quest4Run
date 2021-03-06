package com.comincini_micheli.quest4run.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Equipment;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.List;

/**
 *  Created by Daniele on 01/06/2017.
 */

public class EquipmentShopAdapter extends BaseAdapter {
    private Activity activity;
    private List<Equipment> data;
    private static LayoutInflater inflater=null;
    private int wallet;
    private int level;

    private Equipment equipment_actual;
    private DatabaseHandler db;

    public EquipmentShopAdapter(Activity a, List<Equipment> d, DatabaseHandler db) {
        activity = a;
        data=d;
        this.db = db;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences settings = activity.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        wallet = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1)).getWallet();
        level = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1)).getLevel();
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
            vi = inflater.inflate(R.layout.shop_list_row, null);

        ImageView icon = (ImageView)vi.findViewById(R.id.shop_equipment_icon);
        ImageView icon_coin = (ImageView)vi.findViewById(R.id.shop_equipment_icon_coin);
        TextView name = (TextView)vi.findViewById(R.id.shop_equipment_name);
        TextView minLevel = (TextView)vi.findViewById(R.id.shop_equipment_min_level);
        TextView attack = (TextView)vi.findViewById(R.id.shop_equipment_attack_value);
        TextView defense = (TextView)vi.findViewById(R.id.shop_equipment_defense_value);
        TextView magic = (TextView)vi.findViewById(R.id.shop_equipment_magic_value);
        TextView price = (TextView)vi.findViewById(R.id.shop_equipment_price);
        Button buy_button = (Button) vi.findViewById(R.id.btn_buy);

        equipment_actual = data.get(position);

        String [] icons = activity.getResources().getStringArray(R.array.equipment_icons);
        int id = activity.getResources().getIdentifier(icons[equipment_actual.getIdType()],"drawable", activity.getPackageName());
        icon.setImageResource(id);
        icon.setColorFilter(Color.parseColor(equipment_actual.getIcon()));
        icon_coin.setColorFilter(activity.getResources().getColor(R.color.gold));
        // Setting all values in listview
        name.setText(equipment_actual.getName());
        minLevel.setText(String.format(activity.getResources().getString(R.string.shop_min_level),equipment_actual.getMinLevel()));
        if(equipment_actual.getAtk()>=0)
            attack.setText(String.format(activity.getResources().getString(R.string.value_with_plus_label),equipment_actual.getAtk()));
        else
            attack.setText(String.valueOf(equipment_actual.getAtk()));

        if(equipment_actual.getDef()>=0)
            defense.setText(String.format(activity.getResources().getString(R.string.value_with_plus_label),equipment_actual.getDef()));
        else
            defense.setText(String.valueOf(equipment_actual.getDef()));

        if(equipment_actual.getMgc()>=0)
            magic.setText(String.format(activity.getResources().getString(R.string.value_with_plus_label),equipment_actual.getMgc()));
        else
            magic.setText(String.valueOf(equipment_actual.getMgc()));
        price.setText(String.format(activity.getResources().getString(R.string.money_label),equipment_actual.getPrice()));
        if(wallet < equipment_actual.getPrice() || level < equipment_actual.getMinLevel())
        {
            buy_button.setEnabled(false);
        }
        else
        {
            buy_button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    equipment_actual = data.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(String.format(activity.getResources().getString(R.string.buy_confirmation),equipment_actual.getName(),equipment_actual.getPrice()))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    equipment_actual.setBought(true);
                                    db.updateEquipment(equipment_actual);
                                    data.remove(position);
                                    SharedPreferences settings = activity.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
                                    Character myCharacter = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1));
                                    wallet-=equipment_actual.getPrice();
                                    myCharacter.setWallet(wallet);
                                    db.updateCharacter(myCharacter);
                                    Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.bought_label), Toast.LENGTH_SHORT).show();

                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            });
        }
        return vi;
    }
}
