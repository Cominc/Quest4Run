package com.comincini_micheli.quest4run.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.AddTaskActivity;
import com.comincini_micheli.quest4run.activity.TaskHistoryActivity;
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
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task_option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:

                break;

            case R.id.action_delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_confirmation)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int deleted = db.deleteAllTasks(false);
                                taskList.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), String.format(getResources().getString(R.string.number_task_deleted), deleted), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
                break;

            case R.id.action_show_history:
                Intent intent = new Intent(getActivity(), TaskHistoryActivity.class);
                startActivity(intent);
                break;

            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list_task) {
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
                taskList.get(info.position).setCompleted(true);
                db.updateTask(taskList.get(info.position));
                taskList.remove(info.position);
                adapter.notifyDataSetChanged();
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

        taskList = db.getTasks(false);
        list=(ListView)getView().findViewById(R.id.list_task);
        registerForContextMenu(list);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.btn_create_task);
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
        list.setEmptyView(getActivity().findViewById(R.id.empty_list));
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
