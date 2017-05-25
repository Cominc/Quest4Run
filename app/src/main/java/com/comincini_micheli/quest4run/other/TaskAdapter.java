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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Task;

public class TaskAdapter extends BaseAdapter
{

    private Activity activity;
    private List<Task> data;
    private static LayoutInflater inflater=null;

    public TaskAdapter(Activity a, List<Task> d) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.task_list_row, null);

        TextView name = (TextView)vi.findViewById(R.id.name);
        TextView type = (TextView)vi.findViewById(R.id.type);
        TextView objective = (TextView)vi.findViewById(R.id.objective);

        Task task = new Task();
        task = data.get(position);

        // Setting all values in listview
        //TODO Va bene mettere le stringe fisse così oppure bisognerebbe usare 2 PlainText separati?
        name.setText(task.getName());
        type.setText(String.valueOf(task.getIdTaskType()));
        objective.setText(task.getGoal());

        return vi;
    }
}
