package com.comincini_micheli.quest4run.other;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;

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
    private String [] task_reward;
    private Task task_actual;

    public TaskHistoryAdapter(Activity a, List<Task> d, String [] task_type, String [][] task_goal, String [] task_reward) {
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

        TextView name = (TextView)vi.findViewById(R.id.task_name_completed);
        TextView type = (TextView)vi.findViewById(R.id.task_type_completed);
        TextView goal = (TextView)vi.findViewById(R.id.task_goal_completed);
        TextView reward = (TextView)vi.findViewById(R.id.task_reward_completed);

        task_actual = data.get(position);

        // Setting all values in listview
        //TODO Va bene mettere le stringe fisse così oppure bisognerebbe usare 2 PlainText separati?
        name.setText(task_actual.getName());
        type.setText(task_type[task_actual.getIdTaskType()]);
        goal.setText(task_goal[task_actual.getIdTaskType()][Integer.parseInt(task_actual.getGoal())]);
        reward.setText(task_reward[task_actual.getReward()]);

        return vi;
    }
}
