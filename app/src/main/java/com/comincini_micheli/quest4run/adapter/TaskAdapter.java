package com.comincini_micheli.quest4run.adapter;

/**
 * Created by Gianmaria on 19/05/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;

public class TaskAdapter extends BaseAdapter
{

    private Activity activity;
    private List<Task> data;
    private static LayoutInflater inflater=null;
    private String [] task_type;
    private String [][] task_goal;
    private String [][] task_reward;
    private Task task_actual;
    DatabaseHandler db;

    public TaskAdapter(Activity a, List<Task> d, String [] task_type, String [][] task_goal, String [][] task_reward, DatabaseHandler db) {
        activity = a;
        data=d;
        this.task_type = task_type;
        this.task_goal = task_goal;
        this.task_reward = task_reward;
        this.db = db;
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
            vi = inflater.inflate(R.layout.task_list_row, null);

        ImageView icon = (ImageView)vi.findViewById(R.id.task_icon);
        ImageView icon_coin = (ImageView)vi.findViewById(R.id.icon_coin);
        TextView name = (TextView)vi.findViewById(R.id.task_name);
        TextView type = (TextView)vi.findViewById(R.id.task_type);
        TextView goal = (TextView)vi.findViewById(R.id.task_goal);
        TextView reward = (TextView)vi.findViewById(R.id.task_reward);
        TextView percentage = (TextView)vi.findViewById(R.id.task_completed_percent);
        final Switch active = (Switch) vi.findViewById(R.id.task_switch_active);

        task_actual = data.get(position);

        // Setting all values in listview
        name.setText(task_actual.getName());
        type.setText(task_type[task_actual.getIdTaskType()]);
        goal.setText(task_goal[task_actual.getIdTaskType()][task_actual.getGoal()]);
        reward.setText(task_reward[task_actual.getIdTaskType()][task_actual.getReward()]);
        //TODO collegare a color.xml
        icon_coin.setColorFilter(Color.parseColor("#D4AF37"));

        String [] icons = activity.getResources().getStringArray(R.array.task_icons);
        int id = activity.getResources().getIdentifier(icons[task_actual.getIdTaskType()],"drawable", activity.getPackageName());
        icon.setImageResource(id);

        if(task_actual.getIdTaskType() == Constants.DISTANCE_TYPE_TASK)
        {
            String goalString = task_goal[task_actual.getIdTaskType()][task_actual.getGoal()];
            double goalValue = Double.parseDouble(goalString.substring(0, goalString.length() - 3));
            percentage.setText(Math.round(task_actual.getProgress()/(goalValue*Constants.M_IN_KM)*100) + "%");
        }
        else if(task_actual.getIdTaskType() == Constants.CONSTANCE_TYPE_TASK)
        {
            String goalString = task_goal[task_actual.getIdTaskType()][task_actual.getGoal()];
            double goalValue = Double.parseDouble(goalString.substring(0, goalString.length() - 7));
            percentage.setText(Math.round(task_actual.getProgress()/goalValue*100) + "%");
        }


        active.setChecked(task_actual.isActive());
        active.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                task_actual = data.get(position);
                task_actual.setActive(active.isChecked());
                db.updateTask(task_actual);
            }
        });

        return vi;
    }
}
