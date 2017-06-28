package com.comincini_micheli.quest4run.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QuestDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int idQuest = -1;
        if(b != null)
            idQuest = b.getInt(Constants.ID_QUEST);
        Log.w("id quest",idQuest+"");
        DatabaseHandler db = new DatabaseHandler(this);
        Quest quest = db.getQuest(idQuest);

        TextView title = (TextView) findViewById(R.id.quest_detail_title);
        TextView description = (TextView) findViewById(R.id.quest_detail_description);
        TextView attack = (TextView) findViewById(R.id.quest_detail_attack_value);
        TextView defense = (TextView) findViewById(R.id.quest_detail_defense_value);
        TextView magic = (TextView) findViewById(R.id.quest_detail_magic_value);
        TextView duration = (TextView) findViewById(R.id.quest_detail_duration_value);
        TextView dateFinish = (TextView) findViewById(R.id.quest_detail_date_finish_value);

        title.setText(quest.getTitle());
        description.setText(quest.getDescription());
        attack.setText(quest.getMinAttack()+"");
        defense.setText(quest.getMinDefense()+"");
        magic.setText(quest.getMinMagic()+"");
        duration.setText(quest.getDurationString());
        Date date = new Date(quest.getDateFinish());
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        dateFinish.setText(sdf.format(date));
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
