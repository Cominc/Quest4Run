package com.comincini_micheli.quest4run.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.fragment.CharacterFragment;
import com.comincini_micheli.quest4run.fragment.MapsFragment;
import com.comincini_micheli.quest4run.fragment.QuestFragment;
import com.comincini_micheli.quest4run.fragment.RunFragment;
import com.comincini_micheli.quest4run.fragment.ShopFragment;
import com.comincini_micheli.quest4run.fragment.TaskFragment;
import com.comincini_micheli.quest4run.other.Constants;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean firstOpen;
    int lastFragmentListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle b = getIntent().getExtras();
        if(b != null && b.getBoolean(Constants.FROM_NOTIFICATION_QUEST_COMPLETED))
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new QuestFragment());
            fragmentTransaction.commit();
            this.setTitle(getResources().getString(R.string.nav_quest));
            navigationView.setCheckedItem(R.id.nav_quest);
            lastFragmentListId = R.id.nav_quest;
            Intent i = new Intent(this, QuestHistoryActivity.class);
            startActivity(i);
        }
        else
        {
            //Apro il fragment run
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new RunFragment());
            fragmentTransaction.commit();
            this.setTitle(getResources().getString(R.string.nav_run));
            navigationView.setCheckedItem(R.id.nav_run);
            lastFragmentListId = R.id.nav_run;

            SharedPreferences firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE);
            firstOpen = firstLaunchSetting.getBoolean(Constants.INFO_START_MAIN, true);
            if(firstOpen)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.title_alert_start);
                alert.setMessage(R.string.content_alert_start);
                alert.setPositiveButton(R.string.alert_btn_positive_label, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(firstOpen)
                        {
                            SharedPreferences.Editor firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE).edit();
                            firstLaunchSetting.putBoolean(Constants.INFO_START_MAIN, false);
                            firstLaunchSetting.commit();
                        }
                    }
                });
                alert.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int idNavigationItemSelected = item.getItemId();

        Fragment fragment = null;
        String title = "";

        if(idNavigationItemSelected != R.id.nav_about_us && idNavigationItemSelected != R.id.nav_faq)
        {
            if(idNavigationItemSelected == R.id.nav_path)
            {
                SharedPreferences firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE);
                float distance = firstLaunchSetting.getFloat(Constants.LAST_DISTANCE, 0);
                if(distance > 0)
                    lastFragmentListId = idNavigationItemSelected;
                else
                    lastFragmentListId = R.id.nav_run;
            }
            else
                lastFragmentListId = idNavigationItemSelected;
        }

        switch(idNavigationItemSelected)
        {
            case R.id.nav_run:
                fragment = new RunFragment();
                title = getResources().getString(R.string.nav_run);
                break;
            case R.id.nav_path:
                fragment = new MapsFragment();
                title = getResources().getString(R.string.nav_path);
                break;
            case R.id.nav_task:
                fragment = new TaskFragment();
                title = getResources().getString(R.string.task_fragment_title);
                break;
            case R.id.nav_quest:
                fragment = new QuestFragment();
                title = getResources().getString(R.string.nav_quest);
                break;
            case R.id.nav_shop:
                fragment = new ShopFragment();
                title = getResources().getString(R.string.nav_shop);
                break;
            case R.id.nav_character:
                fragment = new CharacterFragment();
                title = getResources().getString(R.string.nav_character);
                break;
            case R.id.nav_about_us:
                //startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                Intent intent = new Intent(this, AboutUsActivity.class);
                startActivityForResult(intent, Constants.OPEN_ABOUT_US_ACTIVITY);
                break;
            case R.id.nav_faq:
                //startActivity(new Intent(MainActivity.this, FaqActivity.class));
                Intent intent2 = new Intent(this, FaqActivity.class);
                startActivityForResult(intent2, Constants.OPEN_FAQ_ACTIVITY);
                break;
            default:
                fragment = new RunFragment();
                title = getResources().getString(R.string.nav_run);
                break;
        }


        if(fragment!= null)
        {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            this.setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(lastFragmentListId);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
