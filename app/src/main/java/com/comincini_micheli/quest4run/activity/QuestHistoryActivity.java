package com.comincini_micheli.quest4run.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.adapter.QuestHistoryAdapter;
import com.comincini_micheli.quest4run.objects.Quest;
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
        adapter = new QuestHistoryAdapter(this, questList);
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
