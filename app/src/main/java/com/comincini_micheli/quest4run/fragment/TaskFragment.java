package com.comincini_micheli.quest4run.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.TaskAdapter;

import java.util.ArrayList;
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
        DatabaseHandler db = new DatabaseHandler(getContext());
        Task t = new Task();
        t.setName("Corsa 1");
        t.setIdTaskType(55);
        t.setObjective("5 km");
        db.addTask(new Task());
        List<Task> taskList = db.getAllTasks();
        list=(ListView)getActivity().findViewById(R.id.list);

        // Getting adapter by passing xml data ArrayList
        adapter=new TaskAdapter(getActivity(), taskList);
        list.setAdapter(adapter);
        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
