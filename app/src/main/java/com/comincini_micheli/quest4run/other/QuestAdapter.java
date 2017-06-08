package com.comincini_micheli.quest4run.other;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Quest;

import java.util.List;

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
    DatabaseHandler db;

    public QuestAdapter(Activity a, List<Quest> d, DatabaseHandler db) {
        activity = a;
        data=d;
        this.db = db;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indexActiveQuest = -1;
        SharedPreferences settings = activity.getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        idCharacter = settings.getInt(Constants.CHAR_ID_PREFERENCE,-1);
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
        CheckBox active = (CheckBox)vi.findViewById(R.id.quest_active_check_box);

        questActual = data.get(position);

        // Setting all values in listview
        title.setText(questActual.getTitle());
        attack.setText(String.valueOf(questActual.getMinAttack()));
        defense.setText(String.valueOf(questActual.getMinDefense()));
        magic.setText(String.valueOf(questActual.getMinMagic()));
        expReward.setText(questActual.getExpReward() + activity.getResources().getString(R.string.exp_label));
        active.setChecked(questActual.isActive());
        if(questActual.isActive())
            indexActiveQuest = position;

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBoxActive = (CheckBox)view;
                if(checkBoxActive.isChecked())
                {
                    if(indexActiveQuest != -1)
                    {
                        final Quest previusActiveQuest = data.get(indexActiveQuest);
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        String request = String.format(activity.getResources().getString(R.string.request_change_active_quest),previusActiveQuest.getTitle());
                        builder.setMessage(request)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        previusActiveQuest.setActive(false);
                                        db.updateQuest(previusActiveQuest);
                                        data.set(indexActiveQuest,previusActiveQuest);

                                        questActual = data.get(position);
                                        questActual.setActive(true);
                                        db.updateQuest(questActual);
                                        data.set(position, questActual);
                                        indexActiveQuest = position;
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
                    }
                    else
                    {
                        questActual = data.get(position);
                        questActual.setActive(true);
                        db.updateQuest(questActual);
                        data.set(position, questActual);
                        indexActiveQuest = position;
                    }
                }
                else
                {
                    final Quest previusActiveQuest = data.get(indexActiveQuest);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage(R.string.request_deactive_quest)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    previusActiveQuest.setActive(false);
                                    db.updateQuest(previusActiveQuest);
                                    data.set(indexActiveQuest,previusActiveQuest);
                                    indexActiveQuest = -1;
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
                }

                notifyDataSetChanged();
            }
        });

        return vi;
    }
}
