package com.example.dailyplanner.anxiliary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.dailyplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EventListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater ltInflater;
    ArrayList<Event> objects;
    private final String EVENT_KEY = "Events";
    private final String TASK_KEY = "Tasks";

    public EventListAdapter(Context context, ArrayList<Event> products) {
        ctx = context;
        objects = products;
        ltInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void removeEvent(int position) {
        if (position >= 0 && position < objects.size()) {
            Event event = objects.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Подтверждение удаления");
            builder.setMessage("Вы уверены, что хотите удалить эту запись?");

            // Добавляем кнопку "Подтвердить"
            builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference(EVENT_KEY);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()) {
                                Event updateEvent = ds.getValue(Event.class);
                                if (updateEvent.equals(event)) {
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
        Log.d("eventPage", "Start creating item_event");
        View view = convertView;

        if (view == null)
            view = ltInflater.inflate(R.layout.event_item, parent, false);

        Event event = getEvent(position);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        ((TextView) view.findViewById(R.id.text_event_title)).setText(event.getTitle());
        ((TextView) view.findViewById(R.id.text_event_description)).setText(event.getDescription());
        ((TextView) view.findViewById(R.id.dayOfMonth)).setText(String.valueOf(event.getDayOfMonth()));
        ((TextView) view.findViewById(R.id.month)).setText(String.valueOf(event.getMonth()));
        ((TextView) view.findViewById(R.id.year)).setText(String.valueOf(event.getYear()));
        downloadImageFromStorage(event, ((ImageView) view.findViewById(R.id.image_event)));

        ((Button) view.findViewById(R.id.button_delete_event)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEvent(position);
            }
        });

        ((Button) view.findViewById(R.id.button_add_to_task)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask(event.getTitle(), event.getDescription(), event.getDayOfMonth(), event.getMonth(), event.getYear());
            }
        });

        Log.d("eventPage", "End creating item_event");
        return view;
    }

    public void saveTask(String name, String description, int dayOfMonth, int month, int year){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(TASK_KEY);
        Task task = new Task(firebaseAuth.getUid(), name, description, Types.REST, year, month, dayOfMonth, false);
        databaseReference.push().setValue(task);
    }

    public void downloadImageFromStorage(Event event, ImageView imageView) {
        String fileName = event.getImageResId();

        // Получаем ссылку на Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Log.d("eventPage", "Path: " + fileName);
        // Создаем ссылку на файл в Firebase Storage
        StorageReference imageRef = storageRef.child("Event_images/" + fileName);

        // Получаем URL для скачивания
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Используем Glide для загрузки изображения по URI
                Glide.with(ctx)
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("eventPage", "Image is not founded");
                imageView.setImageResource(R.drawable.gray_placholder);
            }
        });
    }

    public ArrayList<Event> getEvents() {
        return objects;
    }

    Event getEvent(int position) {
        return ((Event) getItem(position));
    }
}
