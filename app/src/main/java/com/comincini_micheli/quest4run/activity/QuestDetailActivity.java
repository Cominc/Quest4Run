package com.comincini_micheli.quest4run.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QuestDetailActivity extends AppCompatActivity {

    long countTime;
    Quest quest;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int idQuest = -1;
        if(b != null)
        {
            idQuest = b.getInt(Constants.ID_QUEST);
        }
        db = new DatabaseHandler(this);
        quest = db.getQuest(idQuest);

        TextView title = (TextView) findViewById(R.id.quest_detail_title);
        TextView description = (TextView) findViewById(R.id.quest_detail_description);
        TextView reward = (TextView) findViewById(R.id.quest_detail_exp_reward);
        TextView attack = (TextView) findViewById(R.id.quest_detail_attack_value);
        TextView defense = (TextView) findViewById(R.id.quest_detail_defense_value);
        TextView magic = (TextView) findViewById(R.id.quest_detail_magic_value);
        TextView duration = (TextView) findViewById(R.id.quest_detail_duration_value);
        TextView dateStart = (TextView) findViewById(R.id.quest_detail_date_start_value);
        TextView dateFinish = (TextView) findViewById(R.id.quest_detail_date_finish_value);
        final TextView countdown = (TextView) findViewById(R.id.quest_detail_countdown);

        title.setText(quest.getTitle());
        description.setText(quest.getDescription());
        reward.setText(String.format(getResources().getString(R.string.quest_detail_reward),quest.getExpReward()));
        attack.setText(String.valueOf(quest.getMinAttack()));
        defense.setText(String.valueOf(quest.getMinDefense()));
        magic.setText(String.valueOf(quest.getMinMagic()));
        duration.setText(quest.getDurationString());
        countdown.setVisibility(View.GONE);
        if(quest.getDateStart()!=Quest.DEFAUL_EMPTY_DATE_VALUE) {
            Date date = new Date(quest.getDateStart());
            SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.datetime_format));
            dateStart.setText(sdf.format(date));
        }
        else{
            findViewById(R.id.quest_detail_date_start_label).setVisibility(View.GONE);
            dateStart.setText("");
        }
        if(quest.getDateFinish()!=Quest.DEFAUL_EMPTY_DATE_VALUE) {
            Date date = new Date(quest.getDateFinish());
            SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.datetime_format));
            dateFinish.setText(sdf.format(date));
        }
        else{
            findViewById(R.id.quest_detail_date_finish_label).setVisibility(View.GONE);
            dateFinish.setText("");
        }


        if(quest.isActive()) {
            countdown.setVisibility(View.VISIBLE);
            countTime = quest.getDateStart() + quest.getDuration() - System.currentTimeMillis();
            new CountDownTimer(countTime, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    countTime -= 1000;
                    long durationMS = countTime;
                    long temp;
                    String durationString = "";
                    temp = TimeUnit.MILLISECONDS.toHours(durationMS);
                    if (temp < 10)
                        durationString += "0";
                    durationString += temp + ":";
                    durationMS -= TimeUnit.HOURS.toMillis(temp);
                    temp = TimeUnit.MILLISECONDS.toMinutes(durationMS);
                    if (temp < 10)
                        durationString += "0";
                    durationString += temp + ":";
                    durationMS -= TimeUnit.MINUTES.toMillis(temp);
                    temp = TimeUnit.MILLISECONDS.toSeconds(durationMS);
                    if (temp < 10)
                        durationString += "0";
                    durationString += temp;
                    countdown.setText(durationString);
                }

                @Override
                public void onFinish() {
                    countdown.setText(getResources().getString(R.string.countdown_finish_label));
                }
            }.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause()
    {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        super.onPause();
    }

}
