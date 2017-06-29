package com.comincini_micheli.quest4run.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.EquipmentActivity;
import com.comincini_micheli.quest4run.activity.QuestDetailActivity;
import com.comincini_micheli.quest4run.activity.QuestHistoryActivity;
import com.comincini_micheli.quest4run.activity.TaskHistoryActivity;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.comincini_micheli.quest4run.adapter.QuestAdapter;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestFragment extends Fragment
{

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.quest_list_view) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_quest_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.detail:
                Intent intent = new Intent(getActivity(), QuestDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ID_QUEST, questList.get(info.position).getId());
                intent.putExtras(b);
                startActivityForResult(intent,Constants.DETAILS_QUEST);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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

        /*
        Quest quest1 = new Quest("quest 1", "", 1, 2, 3, 4, 5);
        quest1.setCompleted(true);
        db.addQuest(quest1);
        Quest quest2 = new Quest("quest 2", "", 1, 2, 3, 4, 5);
        db.addQuest(quest2);
        Quest quest3 = new Quest("quest 3", "", 1, 2, 3, 4, 5);
        db.addQuest(quest3);
        */
        Quest activeQuest = db.getActiveQuest();
        if(activeQuest!=null && activeQuest.checkCompleted())
        {
            activeQuest.setCompleted(true);
            activeQuest.setActive(false);
            activeQuest.setDateFinish(activeQuest.getDateStart() + activeQuest.getDuration());
            db.updateQuest(activeQuest);
        }

        questList = db.getQuests(false);
        list=(ListView)getView().findViewById(R.id.quest_list_view);
        registerForContextMenu(list);
        adapter = new QuestAdapter(getActivity(), questList, db);
        list.setAdapter(adapter);
        list.setEmptyView(getActivity().findViewById(R.id.empty_list));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.DETAILS_QUEST)
        {
            Quest activeQuest = db.getActiveQuest();
            if(activeQuest!=null && activeQuest.checkCompleted())
            {
                activeQuest.setCompleted(true);
                activeQuest.setActive(false);
                activeQuest.setDateFinish(activeQuest.getDateStart() + activeQuest.getDuration());
                db.updateQuest(activeQuest);
                questList.remove(activeQuest);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
