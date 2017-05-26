package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class AddTaskActivity extends AppCompatActivity
{
    Spinner spinnerType;
    Spinner spinnerGoal;
    Spinner spinnerReward;
    Button createButton;
    TextView nameTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        spinnerType = (Spinner) findViewById(R.id.taskTypeSpinner);
        //spinner.setOnItemClickListener();
        List<String> listType = Arrays.asList(getResources().getStringArray(R.array.task_type));
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listType);
        spinnerType.setAdapter(adapterType);

        spinnerGoal = (Spinner) findViewById(R.id.taskGoalSpinner);
        //spinner.setOnItemClickListener();
        List<String> listGoal = Arrays.asList(getResources().getStringArray(R.array.task_goal));
        ArrayAdapter<String> adapterGoal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listGoal);
        spinnerGoal.setAdapter(adapterGoal);

        spinnerReward = (Spinner) findViewById(R.id.taskRewardSpinner);
        //spinner.setOnItemClickListener();
        List<String> listReward = Arrays.asList(getResources().getStringArray(R.array.task_reward));
        ArrayAdapter<String> adapterReward = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listReward);
        spinnerReward.setAdapter(adapterReward);

        nameTextEdit = (TextView)findViewById(R.id.nameTask);


        createButton = (Button) findViewById(R.id.btnAddTask);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                Task t = new Task();

                if(nameTextEdit.getText().toString().trim().length() != 0)
                {
                    t.setName(nameTextEdit.getText().toString().trim());
                    t.setIdTaskType(spinnerType.getSelectedItemPosition());
                    t.setGoal(Integer.toString(spinnerGoal.getSelectedItemPosition()));
                    t.setReward(spinnerReward.getSelectedItemPosition());
                    db.addTask(t);
                    db.close();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.TASK_ADDED, t);
                    setResult(RESULT_OK, resultIntent);

                    Toast.makeText(getApplicationContext(),R.string.task_added,Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    nameTextEdit.setError("Inserire il nome");
                }
            }
        });
    }
}
