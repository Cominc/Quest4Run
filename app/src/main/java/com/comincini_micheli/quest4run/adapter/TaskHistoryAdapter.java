package com.comincini_micheli.quest4run.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Gianmaria on 29/05/2017.
 */

public class TaskHistoryAdapter extends BaseAdapter
{

    private Activity activity;
    private List<Task> data;
    private static LayoutInflater inflater=null;
    private String [] task_type;
    private String [][] task_goal;
    private String [][] task_reward;
    private Task task_actual;

    public TaskHistoryAdapter(Activity a, List<Task> d, String [] task_type, String [][] task_goal, String [][] task_reward) {
        activity = a;
        data=d;
        this.task_type = task_type;
        this.task_goal = task_goal;
        this.task_reward = task_reward;
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.task_list_completed_row, null);

        ImageView icon = (ImageView)vi.findViewById(R.id.task_icon_completed);
        ImageView icon_coin = (ImageView)vi.findViewById(R.id.icon_coin_completed);
        TextView name = (TextView)vi.findViewById(R.id.task_name_completed);
        TextView type = (TextView)vi.findViewById(R.id.task_type_completed);
        TextView goal = (TextView)vi.findViewById(R.id.task_goal_completed);
        TextView reward = (TextView)vi.findViewById(R.id.task_reward_completed);
        TextView dateComplete = (TextView)vi.findViewById(R.id.text_view_date_completed);

        task_actual = data.get(position);

        // Setting all values in listview
        name.setText(task_actual.getName());
        type.setText(task_type[task_actual.getIdTaskType()]);
        goal.setText(task_goal[task_actual.getIdTaskType()][task_actual.getGoal()]);
        reward.setText(task_reward[task_actual.getIdTaskType()][task_actual.getReward()]);

        String [] icons = activity.getResources().getStringArray(R.array.task_icons);
        int id = activity.getResources().getIdentifier(icons[task_actual.getIdTaskType()],"drawable", activity.getPackageName());
        icon.setImageResource(id);
        //TODO collegare a color.xml
        icon_coin.setColorFilter(Color.parseColor("#D4AF37"));

        Date date = new Date(task_actual.getExecDate());
        SimpleDateFormat sdf = new SimpleDateFormat(activity.getResources().getString(R.string.date_format));
        dateComplete.setText(sdf.format(date));

        return vi;
    }
}
