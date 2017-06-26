package com.comincini_micheli.quest4run.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.adapter.TaskHistoryAdapter;

import java.util.List;

public class TaskHistoryActivity extends AppCompatActivity
{
    List<Task> taskList;
    ListView list;
    TaskHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHandler db = new DatabaseHandler(this);

        taskList = db.getTasks(true);
        list=(ListView) findViewById(R.id.list_task_completed);

        // Getting adapter by passing xml data ArrayList
        String [][] taskGoals = {getResources().getStringArray(R.array.task_distance_goal),
                getResources().getStringArray(R.array.task_rithm_goal),
                getResources().getStringArray(R.array.task_constance_goal),
                getResources().getStringArray(R.array.task_duration_goal)};

        adapter=new TaskHistoryAdapter(this, taskList, getResources().getStringArray(R.array.task_type),
                taskGoals, getResources().getStringArray(R.array.task_reward));
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.empty_list));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
