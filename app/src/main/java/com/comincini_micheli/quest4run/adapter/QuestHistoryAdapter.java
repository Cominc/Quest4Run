package com.comincini_micheli.quest4run.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.util.List;

/**
 * Created by Daniele on 27/06/2017.
 */

public class QuestHistoryAdapter extends BaseAdapter
{
    private Activity activity;
    private List<Quest> data;
    private static LayoutInflater inflater=null;

    private Quest questActual;

    public QuestHistoryAdapter(Activity a, List<Quest> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.quest_history_list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.quest_history_title);
        TextView attack = (TextView)vi.findViewById(R.id.quest_history_min_attack);
        TextView defense = (TextView)vi.findViewById(R.id.quest_history_min_defense);
        TextView magic = (TextView)vi.findViewById(R.id.quest_history_min_magic);
        TextView expReward = (TextView) vi.findViewById(R.id.quest_history_exp_reward);

        questActual = data.get(position);

        // Setting all values in listview
        title.setText(questActual.getTitle());
        attack.setText(String.valueOf(questActual.getMinAttack()));
        defense.setText(String.valueOf(questActual.getMinDefense()));
        magic.setText(String.valueOf(questActual.getMinMagic()));
        expReward.setText(questActual.getExpReward() + activity.getResources().getString(R.string.exp_label));

        return vi;
    }
}
