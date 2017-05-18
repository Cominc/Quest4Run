package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.comincini_micheli.quest4run.R;

public class SplashActivity extends AppCompatActivity {

    int TIMEOUT_SPLASH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, TrainingActivity.class);
                startActivity(i);
                finish();
            }
        }, TIMEOUT_SPLASH);
    }
}
