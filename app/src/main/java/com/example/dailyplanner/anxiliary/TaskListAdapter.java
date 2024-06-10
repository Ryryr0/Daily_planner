package com.example.dailyplanner.anxiliary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dailyplanner.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater ltInflater;
    ArrayList<Task> objects;
    private final String TASK_KEY = "Tasks";

    public TaskListAdapter(Context context, ArrayList<Task> products) {
        ctx = context;
        objects = products;
        ltInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void removeTask(int position) {
        if (position >= 0 && position < objects.size()) {
            Task task = objects.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Подтверждение удаления");
            builder.setMessage("Вы уверены, что хотите удалить эту запись?");

            // Добавляем кнопку "Подтвердить"
            builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference(TASK_KEY);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                Task updateTask = ds.getValue(Task.class);
                                if (updateTask.equals(task)) {
                                    databaseReference.child(ds.getKey()).removeValue();
                                    return;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    objects.remove(position);
                    notifyDataSetChanged();
                }
            });

            // Добавляем кнопку "Отмена"
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Закрываем диалоговое окно
                    dialog.dismiss();
                }
            });

            // Создаем и отображаем AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
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
        ((CheckBox) view.findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setComplete(isChecked);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference(TASK_KEY);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            Task updateTask = ds.getValue(Task.class);
                            if (updateTask.equals(task)) {
                                databaseReference.child(ds.getKey()).setValue(task);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        ((ImageView) view.findViewById(R.id.iconImageView)).setImageResource(task.getType().getTitle());
        ((Button) view.findViewById(R.id.buttonItemDelete)).setVisibility(View.GONE);
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
