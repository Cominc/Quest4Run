package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.TaskAdapter;
import com.comincini_micheli.quest4run.other.TaskHistoryAdapter;

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
        DatabaseHandler db = new DatabaseHandler(this);

        taskList = db.getTasks(true);
        list=(ListView) findViewById(R.id.list_task_completed);

        // Getting adapter by passing xml data ArrayList
        adapter=new TaskHistoryAdapter(this, taskList, getResources().getStringArray(R.array.task_type),
                getResources().getStringArray(R.array.task_goal), getResources().getStringArray(R.array.task_reward));
        list.setAdapter(adapter);
    }
}
