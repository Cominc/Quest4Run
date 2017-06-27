package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.adapter.QuestHistoryAdapter;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.List;

public class QuestHistoryActivity extends AppCompatActivity {
    private ListView list;
    private QuestHistoryAdapter adapter;
    private List<Quest> questList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_history);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHandler(this);
        questList = db.getQuests(true);
        list=(ListView)findViewById(R.id.quest_history_list_view);
        registerForContextMenu(list);
        adapter = new QuestHistoryAdapter(this, questList);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.empty_list));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.quest_history_list_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_quest_list, menu);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.detail:
                Intent intent = new Intent(this, QuestDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt(Constants.ID_QUEST, questList.get(info.position).getId());
                intent.putExtras(b);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
