package com.comincini_micheli.quest4run.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.AddTaskActivity;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.TaskAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class TaskFragment extends Fragment
{
    ListView list;
    TaskAdapter adapter;
    List<Task> taskList;
    DatabaseHandler db;

    public TaskFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        Log.w("fragment create","");
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        return v;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_tasklist, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DatabaseHandler db = new DatabaseHandler(getContext());

        switch(item.getItemId()) {
            case R.id.edit:
                // edit stuff here
                //TODO EDIT CONTESTUALE TASK
                return true;
            case R.id.delete:
                // remove stuff here
                db.deleteTask(taskList.get(info.position));
                taskList.remove(info.position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),R.string.task_delete, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHandler(getContext());

        Log.w("lista riletta db","");
        taskList = db.getAllTasks();
        list=(ListView)getView().findViewById(R.id.list);
        registerForContextMenu(list);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(intent, Constants.CREATE_TASK_REQUEST);
            }
        });

        // Getting adapter by passing xml data ArrayList
        adapter=new TaskAdapter(getActivity(), taskList, getResources().getStringArray(R.array.task_type),
                                getResources().getStringArray(R.array.task_goal), getResources().getStringArray(R.array.task_reward), db);
        list.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.CREATE_TASK_REQUEST)
        {
            if(resultCode == RESULT_OK)
            {
                taskList.add((Task) data.getSerializableExtra(Constants.TASK_ADDED));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
