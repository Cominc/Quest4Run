package com.comincini_micheli.quest4run.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.other.Constants;

public class EquipmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        Bundle b = getIntent().getExtras();
        int type = -1;
        String title = "";
        if(b != null)
            type = b.getInt("type");
        switch(type)
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
    }
}
