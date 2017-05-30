package com.comincini_micheli.quest4run.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.fragment.TaskFragment;
import com.comincini_micheli.quest4run.fragment.TestFragment;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //TODO RIMUOVERE TUTTI I TASK
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int idNavigationItemSelected = item.getItemId();

        Fragment fragment = null;
        String title = "";

        switch(idNavigationItemSelected)
        {
            case R.id.nav_task:
                fragment = new TaskFragment();
                title = getResources().getString(R.string.task_fragment_title);
                break;
            case R.id.nav_quest:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_quest);
                break;
            case R.id.nav_run:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_run);
                break;
            case R.id.nav_shop:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_shop);
                break;
            case R.id.nav_character:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_character);
                break;
            case R.id.nav_share:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_share);
                break;
            case R.id.nav_send:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_send);
                break;
            case R.id.nav_about_us:
                fragment = new TestFragment();
                title = getResources().getString(R.string.nav_about_us);
                break;
            default:
                fragment = new TestFragment();
                title = getResources().getString(R.string.test_fragment_title);
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
}
