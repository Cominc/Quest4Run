package com.comincini_micheli.quest4run.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.EquipmentInventoryAdapter;
import com.comincini_micheli.quest4run.other.EquipmentShopAdapter;

import java.util.List;

public class EquipmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        Bundle b = getIntent().getExtras();
        int equipmentTypeId = -1;
        String title = "";
        if(b != null)
            equipmentTypeId = b.getInt("type");
        switch(equipmentTypeId)
        {
            case Constants.ID_TYPE_ATTACK:
                title = getResources().getString(R.string.inventory_attack);
                break;
            case Constants.ID_TYPE_DEFENSE:
                title =getResources().getString(R.string.inventory_defense);
                break;
            case Constants.ID_TYPE_MAGIC:
                title = getResources().getString(R.string.inventory_magic);
                break;
        }
        setTitle(title);

        DatabaseHandler db = new DatabaseHandler(this);
        List equipmentList = db.getAllEquipments(equipmentTypeId,true);
        ListView list = (ListView) findViewById(R.id.list_equipment_inventory);


        // Getting adapter by passing xml data ArrayList
        EquipmentInventoryAdapter adapter = new EquipmentInventoryAdapter(this, equipmentList, db);
        list.setAdapter(adapter);
    }
}
