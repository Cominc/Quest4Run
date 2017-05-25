package com.comincini_micheli.quest4run.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.TaskAdapter;

import java.util.List;

public class TaskFragment extends Fragment
{
    ListView list;
    TaskAdapter adapter;
    List<Task> taskList;

    public TaskFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        /*
        DatabaseHandler db = new DatabaseHandler(getContext());
        Task t = new Task();
        t.setName("Corsa 1");
        t.setIdTaskType(55);
        t.setObjective("5 km");
        db.addTask(new Task());
        List<Task> taskList = db.getAllTasks();

        list=(ListView)getView().findViewById(R.id.list);

        // Getting adapter by passing xml data ArrayList
        adapter=new TaskAdapter(getActivity(), taskList);
        list.setAdapter(adapter);
        */
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
                Toast.makeText(getContext(),"siamo in edit", Toast.LENGTH_LONG).show();
                Log.w("menu","edit");
                return true;
            case R.id.delete:
                // remove stuff here
                db.deleteTask(taskList.get(info.position));
                taskList.remove(info.position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),R.string.task_delete, Toast.LENGTH_LONG).show();
                //Log.w("menu","delete " + taskList.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(getContext());
        /*Task t = new Task();
        t.setName("Corsa 1");
        t.setIdTaskType(55);
        t.setObjective("5 km");
        db.addTask(t);
        t.setName("Corsa 2");
        t.setIdTaskType(55);
        t.setObjective("5 km");
        db.addTask(t);
        t.setName("Corsa 3");
        t.setIdTaskType(55);
        t.setObjective("5 km");
        db.addTask(t);*/
        taskList = db.getAllTasks();
        db.close();
        list=(ListView)getView().findViewById(R.id.list);
        registerForContextMenu(list);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Task t = new Task();
                t.setName("Corsa add");
                t.setIdTaskType(55);
                t.setObjective("5 km");
                DatabaseHandler db = new DatabaseHandler(getContext());
                db.addTask(t);
                db.close();*/
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        // Getting adapter by passing xml data ArrayList
        adapter=new TaskAdapter(getActivity(), taskList);
        list.setAdapter(adapter);
    }
}
