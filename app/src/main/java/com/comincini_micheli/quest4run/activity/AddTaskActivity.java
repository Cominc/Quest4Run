package com.comincini_micheli.quest4run.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class AddTaskActivity extends AppCompatActivity
{
    Spinner spinnerType;
    Spinner spinnerGoal;
    Spinner spinnerReward;
    Button createButton;
    TextView nameTextEdit;

    private boolean firstOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences firstLaunchSetting = getSharedPreferences(Constants.NAME_PREFS, MODE_PRIVATE);
        firstOpen = firstLaunchSetting.getBoolean(Constants.INFO_ADD_TASK, true);
        if(firstOpen)
        {
            showInfo();
        }

        spinnerType = (Spinner) findViewById(R.id.task_type_spinner);
        //spinner.setOnItemClickListener();
        List<String> listType = Arrays.asList(getResources().getStringArray(R.array.task_type));
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listType);
        spinnerType.setAdapter(adapterType);

        spinnerGoal = (Spinner) findViewById(R.id.task_goal_spinner);
        //spinner.setOnItemClickListener();
        List<String> listGoal = Arrays.asList(getResources().getStringArray(R.array.task_distance_goal));
        ArrayAdapter<String> adapterGoal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listGoal);
        spinnerGoal.setAdapter(adapterGoal);

        spinnerReward = (Spinner) findViewById(R.id.task_reward_spinner);
        //spinner.setOnItemClickListener();
        List<String> listReward = Arrays.asList(getResources().getStringArray(R.array.task_distance_reward));
        ArrayAdapter<String> adapterReward = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listReward);
        spinnerReward.setAdapter(adapterReward);

        final Context context = this;

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                List<String> listGoal = null;
                List<String> listReward = null;
                switch (position)
                {
                    case Constants.DISTANCE_TYPE_TASK:
                        listGoal = Arrays.asList(getResources().getStringArray(R.array.task_distance_goal));
                        listReward = Arrays.asList(getResources().getStringArray(R.array.task_distance_reward));
                        break;
                    case Constants.PACE_TYPE_TASK:
                        listGoal = Arrays.asList(getResources().getStringArray(R.array.task_rithm_goal));
                        listReward = Arrays.asList(getResources().getStringArray(R.array.task_rithm_reward));
                        break;
                    case Constants.CONSTANCE_TYPE_TASK:
                        listGoal = Arrays.asList(getResources().getStringArray(R.array.task_constance_goal));
                        listReward = Arrays.asList(getResources().getStringArray(R.array.task_constance_reward));
                        break;
                    case Constants.DURATION_TYPE_TASK:
                        listGoal = Arrays.asList(getResources().getStringArray(R.array.task_duration_goal));
                        listReward = Arrays.asList(getResources().getStringArray(R.array.task_duration_reward));
                        break;
                    default:
                        break;
                }
                ArrayAdapter<String> adapterGoal = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listGoal);
                spinnerGoal.setAdapter(adapterGoal);
                ArrayAdapter<String> adapterReward = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, listReward);
                spinnerReward.setAdapter(adapterReward);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        nameTextEdit = (TextView)findViewById(R.id.name_task);


        createButton = (Button) findViewById(R.id.btn_add_task);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Task t = new Task();

                if(nameTextEdit.getText().toString().trim().length() != 0)
                {
                    t.setName(nameTextEdit.getText().toString().trim());
                    t.setIdTaskType(spinnerType.getSelectedItemPosition());
                    t.setGoal(spinnerGoal.getSelectedItemPosition());
                    t.setReward(spinnerReward.getSelectedItemPosition());
                    int newTaskId = db.addTask(t);
                    db.close();
                    t.setId(newTaskId);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.TASK_ADDED, t);
                    setResult(RESULT_OK, resultIntent);

                    Toast.makeText(getApplicationContext(),R.string.task_added,Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    nameTextEdit.setError(getResources().getString(R.string.name_empty_task_error));
                }
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
                    firstLaunchSetting.putBoolean(Constants.INFO_ADD_TASK, false);
                    firstLaunchSetting.commit();
                }
            }
        });
        alert.show();
    }
}
