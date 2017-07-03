package com.comincini_micheli.quest4run.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.AlarmNotificationReceiver;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Gianmaria on 08/06/2017.
 */

public class QuestAdapter extends BaseAdapter
{
    private Activity activity;
    private List<Quest> data;
    private static LayoutInflater inflater=null;

    private Quest questActual;
    private int indexActiveQuest;
    private int idCharacter;

    private int myAttack;
    private int myDefense;
    private int myMagic;

    private long countTime;

    DatabaseHandler db;
    Quest previusActiveQuest;

    public QuestAdapter(Activity a, List<Quest> d, DatabaseHandler db) {
        activity = a;
        data=d;
        this.db = db;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexActiveQuest = -1;
        previusActiveQuest = null;
        SharedPreferences settings = activity.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        idCharacter = settings.getInt(Constants.CHAR_ID_PREFERENCE,-1);
        Character character = db.getCharacter(idCharacter);
        myAttack = character.getAttack();
        myDefense = character.getDefense();
        myMagic = character.getMagic();
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.quest_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.quest_title);
        TextView attack = (TextView)vi.findViewById(R.id.quest_min_attack);
        TextView defense = (TextView)vi.findViewById(R.id.quest_min_defense);
        TextView magic = (TextView)vi.findViewById(R.id.quest_min_magic);
        TextView expReward = (TextView) vi.findViewById(R.id.quest_exp_reward);
        RadioButton active = (RadioButton)vi.findViewById(R.id.quest_active_radiobutton);

        final TextView countdown = (TextView)vi.findViewById(R.id.quest_timer);

        questActual = data.get(position);

        // Setting all values in listview
        title.setText(questActual.getTitle());
        attack.setText(String.valueOf(questActual.getMinAttack()));
        defense.setText(String.valueOf(questActual.getMinDefense()));
        magic.setText(String.valueOf(questActual.getMinMagic()));
        expReward.setText(questActual.getExpReward() + activity.getResources().getString(R.string.exp_label));

        if(!questActual.isActive()){
                countdown.setText("");
        }
        else {
            countTime = questActual.getDateStart() + questActual.getDuration() - System.currentTimeMillis();
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
                    countdown.setText("00:00:00");
                }
            }.start();
        }

        if((questActual.getMinAttack()<= myAttack) && (questActual.getMinDefense() <= myDefense) && (questActual.getMinMagic() <= myMagic))
        {
            active.setChecked(questActual.isActive());
            if(questActual.isActive())
                indexActiveQuest = position;

            active.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        RadioButton radioButtonActive = (RadioButton) v;
                        if(!radioButtonActive.isChecked())
                        {
                            if(indexActiveQuest != -1)
                            {
                                previusActiveQuest = data.get(indexActiveQuest);
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                String request = String.format(activity.getResources().getString(R.string.request_change_active_quest),previusActiveQuest.getTitle());
                                builder.setMessage(request)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                previusActiveQuest.setActive(false);
                                                previusActiveQuest.setDateStart(Quest.DEFAUL_EMPTY_DATE_VALUE);
                                                db.updateQuest(previusActiveQuest);
                                                data.set(indexActiveQuest,previusActiveQuest);

                                                questActual = data.get(position);
                                                questActual.setActive(true);
                                                questActual.setDateStart(System.currentTimeMillis());
                                                db.updateQuest(questActual);
                                                data.set(position, questActual);
                                                indexActiveQuest = position;
                                                notifyDataSetChanged();
                                                startAlarm(questActual.getDuration());
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                notifyDataSetChanged();
                                            }
                                        });
                                // Create the AlertDialog object and return it
                                builder.create();
                                builder.show();
                            }
                            else
                            {
                                questActual = data.get(position);
                                questActual.setActive(true);
                                questActual.setDateStart(System.currentTimeMillis());
                                db.updateQuest(questActual);
                                data.set(position, questActual);
                                indexActiveQuest = position;
                                notifyDataSetChanged();
                                startAlarm(questActual.getDuration());
                            }
                        }
                        else
                        {
                            previusActiveQuest = data.get(position);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage(R.string.request_deactive_quest)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            previusActiveQuest.setActive(false);
                                            previusActiveQuest.setDateStart(Quest.DEFAUL_EMPTY_DATE_VALUE);
                                            db.updateQuest(previusActiveQuest);
                                            data.set(indexActiveQuest,previusActiveQuest);
                                            indexActiveQuest = -1;
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            notifyDataSetChanged();
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create();
                            builder.show();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        else
        {
            if(questActual.isActive())
            {
                questActual.setActive(false);
                questActual.setDateStart(Quest.DEFAUL_EMPTY_DATE_VALUE);
                db.updateQuest(questActual);
            }

            active.setAlpha(0.5f);
            active.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ((RadioButton) v).setChecked(false);
                    Toast.makeText(activity.getApplicationContext(), R.string.impossible_active_quest, Toast.LENGTH_SHORT).show();
                }
            });
        }
        return vi;
    }

    private void startAlarm(long duration) {
        AlarmManager manager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        myIntent = new Intent(activity,AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(activity,0,myIntent,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+duration,pendingIntent);
    }
}
