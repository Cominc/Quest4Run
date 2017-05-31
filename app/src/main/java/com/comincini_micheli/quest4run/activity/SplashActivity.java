package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.other.Constants;

public class SplashActivity extends AppCompatActivity {

    int TIMEOUT_SPLASH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE);
                int characterId = firstLaunchSetting.getInt(Constants.CHAR_ID_PREFERENCE, -1);
                if(characterId == -1)
                {
                    Intent i = new Intent(SplashActivity.this, CreateCharacterActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, TIMEOUT_SPLASH);
    }
}
