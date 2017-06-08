package com.comincini_micheli.quest4run.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.other.QuestAdapter;
import com.comincini_micheli.quest4run.other.TaskAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestFragment extends Fragment
{
    private ListView list;
    private QuestAdapter adapter;
    private List<Quest> questList;
    private DatabaseHandler db;

    public QuestFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quest, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHandler(getContext());

    }
}
