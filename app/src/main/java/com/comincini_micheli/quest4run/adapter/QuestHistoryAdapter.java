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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Quest;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
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

        ImageView icon = (ImageView)vi.findViewById(R.id.quest_history_icon);
        TextView title = (TextView) vi.findViewById(R.id.quest_history_title);
        TextView expReward = (TextView) vi.findViewById(R.id.quest_history_exp_reward);
        TextView dateFinish = (TextView) vi.findViewById(R.id.quest_history_date_finish);

        questActual = data.get(position);

        int id = activity.getResources().getIdentifier(questActual.getIcon(),"drawable", activity.getPackageName());
        icon.setImageResource(id);

        vi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.openContextMenu(v);
            }
        });

        // Setting all values in listview
        title.setText(questActual.getTitle());
        expReward.setText(questActual.getExpReward() + activity.getResources().getString(R.string.exp_label));
        Date date = new Date(questActual.getDateFinish());
        SimpleDateFormat sdf = new SimpleDateFormat(activity.getResources().getString(R.string.date_format));
        dateFinish.setText(sdf.format(date));

        return vi;
    }
}
