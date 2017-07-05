package com.comincini_micheli.quest4run.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.other.Constants;


public class AboutUsActivity extends AppCompatActivity {

    private boolean firstOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.about_us_layout);
        SharedPreferences firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE);
        firstOpen = firstLaunchSetting.getBoolean(Constants.INFO_ABOUT_US, true);
        if(firstOpen)
        {
            showInfo();
        }
        layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.info_button:
                showInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //TODO serve fare Info in questa activity?
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    private void showInfo()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Titolo");
        alert.setMessage("Prova");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(firstOpen)
                {
                    SharedPreferences.Editor firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE).edit();
                    firstLaunchSetting.putBoolean(Constants.INFO_ABOUT_US, false);
                    firstLaunchSetting.commit();
                }
            }
        });
        alert.show();
    }
}
