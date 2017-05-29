package com.comincini_micheli.quest4run.other;

/**
 * Created by Gianmaria on 19/05/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.fragment.TaskFragment;
import com.comincini_micheli.quest4run.objects.Task;

public class TaskAdapter extends BaseAdapter
{

    private Activity activity;
    private List<Task> data;
    private static LayoutInflater inflater=null;
    private TaskFragment fragment;
    private String [] task_type;
    private String [] task_goal;
    private String [] task_reward;
    private Task task_actual;
    DatabaseHandler db;

    public TaskAdapter(Activity a, List<Task> d, String [] task_type, String [] task_goal, String [] task_reward, DatabaseHandler db) {
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
            vi = inflater.inflate(R.layout.task_list_row2, null);

        TextView name = (TextView)vi.findViewById(R.id.task_name);
        TextView type = (TextView)vi.findViewById(R.id.task_type);
        TextView goal = (TextView)vi.findViewById(R.id.task_goal);
        TextView reward = (TextView)vi.findViewById(R.id.task_reward);
        final Switch active = (Switch) vi.findViewById(R.id.task_switch_active);

        task_actual = data.get(position);

        // Setting all values in listview
        //TODO Va bene mettere le stringe fisse così oppure bisognerebbe usare 2 PlainText separati?
        name.setText(task_actual.getName());
        type.setText(task_type[task_actual.getIdTaskType()]);
        goal.setText(task_goal[Integer.parseInt(task_actual.getGoal())]);
        reward.setText(task_reward[task_actual.getReward()]);
        active.setChecked(task_actual.isActive());
        active.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                task_actual = data.get(position);
                task_actual.setActive(active.isChecked());
                //TODO update di active nel db non funziona
                db.updateTask(task_actual);
            }
        });

        return vi;
    }
}
