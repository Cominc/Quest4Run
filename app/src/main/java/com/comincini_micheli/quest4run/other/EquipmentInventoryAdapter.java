package com.comincini_micheli.quest4run.other;

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

import java.util.List;

/**
 * Created by Daniele on 01/06/2017.
 */

public class EquipmentInventoryAdapter extends BaseAdapter {
    private Activity activity;
    private List<Equipment> data;
    private static LayoutInflater inflater=null;

    private Equipment equipment_actual;
    DatabaseHandler db;

    public EquipmentInventoryAdapter(Activity a, List<Equipment> d, DatabaseHandler db) {
        activity = a;
        data=d;
        this.db = db;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        TextView name = (TextView)vi.findViewById(R.id.inventory_equipment_name);
        TextView attack = (TextView)vi.findViewById(R.id.inventory_equipment_attack);
        TextView defense = (TextView)vi.findViewById(R.id.inventory_equipment_defense);
        TextView magic = (TextView)vi.findViewById(R.id.inventory_equipment_magic);

        equipment_actual = data.get(position);

        // Setting all values in listview
        name.setText(equipment_actual.getName());
        attack.setText(String.valueOf(equipment_actual.getAtk()));
        defense.setText(String.valueOf(equipment_actual.getDef()));
        magic.setText(String.valueOf(equipment_actual.getMgc()));

        return vi;
    }
}
