package com.example.dailyplanner.anxiliary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dailyplanner.R;

import java.util.ArrayList;

public class TaskListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater ltInflater;
    ArrayList<Task> objects;



    public TaskListAdapter(Context context, ArrayList<Task> products) {
        ctx = context;
        objects = products;
        ltInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void removeTask(int position) {
        if (position >= 0 && position < objects.size()) {
            objects.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null)
            view = ltInflater.inflate(R.layout.item_task, parent, false);

        Task task = getTask(position);

        ((TextView) view.findViewById(R.id.titleTextView)).setText(task.getName());
        ((TextView) view.findViewById(R.id.descriptionTextView)).setText(task.getDescription());
        ((CheckBox) view.findViewById(R.id.checkBox)).setChecked(task.isComplete());
        ((ImageView) view.findViewById(R.id.iconImageView)).setImageResource(task.getType().getTitle());
        ((Button) view.findViewById(R.id.buttonItemDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTask(position);
            }
        });

        return view;
    }

    public void onDeleteMode(boolean mode, ViewGroup parent){
        for (int i = 0; i < objects.size(); i++){
            if (mode){
                ((Button) getView(i, null, parent)
                        .findViewById(R.id.buttonItemDelete)).setVisibility(View.VISIBLE);
            }else {
                ((Button) getView(i, null, parent)
                        .findViewById(R.id.buttonItemDelete)).setVisibility(View.GONE);
            }
        }
    }

    public ArrayList<Task> getTasks() {
        return objects;
    }

    Task getTask(int position) {
        return ((Task) getItem(position));
    }
}
