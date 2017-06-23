package com.comincini_micheli.quest4run.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Equipment;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.List;

/**
 * Created by Daniele on 01/06/2017.
 */

public class EquipmentShopAdapter extends BaseAdapter {
    private Activity activity;
    private List<Equipment> data;
    private static LayoutInflater inflater=null;
    private int wallet;

    private Equipment equipment_actual;
    DatabaseHandler db;

    public EquipmentShopAdapter(Activity a, List<Equipment> d, DatabaseHandler db) {
        activity = a;
        data=d;
        this.db = db;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SharedPreferences settings = activity.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        wallet = db.getCharacter(settings.getInt(Constants.CHAR_ID_PREFERENCE,-1)).getWallet();
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

        TextView name = (TextView)vi.findViewById(R.id.shop_equipment_name);
        TextView attack = (TextView)vi.findViewById(R.id.shop_equipment_attack);
        TextView defense = (TextView)vi.findViewById(R.id.shop_equipment_defense);
        TextView magic = (TextView)vi.findViewById(R.id.shop_equipment_magic);
        TextView price = (TextView)vi.findViewById(R.id.shop_equipment_price);
        Button buy_button = (Button) vi.findViewById(R.id.btn_buy);

        equipment_actual = data.get(position);

        // Setting all values in listview
        name.setText(equipment_actual.getName());
        attack.setText(String.valueOf(equipment_actual.getAtk()));
        defense.setText(String.valueOf(equipment_actual.getDef()));
        magic.setText(String.valueOf(equipment_actual.getMgc()));
        price.setText(String.valueOf(equipment_actual.getPrice()));
        if(wallet < equipment_actual.getPrice())
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
                    //TODO come usare resources
                    builder.setMessage(String.format("Vuoi acquistare %s per %d " + activity.getResources().getString(R.string.cents_symbol_label) + " ?",equipment_actual.getName(),equipment_actual.getPrice()))
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
