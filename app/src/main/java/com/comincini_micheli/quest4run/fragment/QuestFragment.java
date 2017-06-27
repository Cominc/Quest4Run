package com.comincini_micheli.quest4run.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.QuestHistoryActivity;
import com.comincini_micheli.quest4run.activity.TaskHistoryActivity;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.adapter.QuestAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestFragment extends Fragment
{

    //TODO CREARE STORICO QUEST
    //TODO GESTIRE INIZIO E FINE QUEST
    private ListView list;
    private QuestAdapter adapter;
    private List<Quest> questList;
    private DatabaseHandler db;

    public QuestFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.quest_option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_show_history:
                Intent intent = new Intent(getActivity(), QuestHistoryActivity.class);
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
        return inflater.inflate(R.layout.fragment_quest, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHandler(getContext());

        /*Quest quest1 = new Quest("quest 1", "", 1, 2, 3, 4, 5);
        db.addQuest(quest1);
        Quest quest2 = new Quest("quest 2", "", 1, 2, 3, 4, 5);
        db.addQuest(quest2);
        Quest quest3 = new Quest("quest 3", "", 1, 2, 3, 4, 5);
        db.addQuest(quest3);*/

        questList = db.getQuests(false);
        list=(ListView)getView().findViewById(R.id.quest_list_view);
        adapter = new QuestAdapter(getActivity(), questList, db);
        list.setAdapter(adapter);
        list.setEmptyView(getActivity().findViewById(R.id.empty_list));
    }
}
