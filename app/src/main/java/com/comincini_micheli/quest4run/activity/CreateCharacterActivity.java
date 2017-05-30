package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.comincini_micheli.quest4run.R;

public class CreateCharacterActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_character);

        Button btn = (Button) findViewById(R.id.btn_create_character);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor firstLaunchSetting = getSharedPreferences("my_prefs", MODE_PRIVATE).edit();
                firstLaunchSetting.putBoolean("firstLaunch", false);
                if(firstLaunchSetting.commit())
                {
                    Intent i = new Intent(CreateCharacterActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else
                {
                    Log.w("Errore","Salvataggio non riuscito");
                }
            }
        });
    }
}
