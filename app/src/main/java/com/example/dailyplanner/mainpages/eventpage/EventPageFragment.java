package com.example.dailyplanner.mainpages.eventpage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.activities.MainActivity;
import com.example.dailyplanner.anxiliary.Event;
import com.example.dailyplanner.anxiliary.EventListAdapter;
import com.example.dailyplanner.anxiliary.User;
import com.example.dailyplanner.databinding.FragmentEventPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventPageFragment extends Fragment {

    FragmentEventPageBinding binding;
    private EventListAdapter eventListAdapter;
    private final String EVENT_KEY = "Events";
    private ArrayList<Event> eventList;
    private String role = "user";
    private final String USER_KEY = "Users";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventPageBinding.inflate(inflater, container, false);
        eventList = new ArrayList<Event>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(USER_KEY);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("profileLog", "Start loading data");
                for (DataSnapshot ds: snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user.getId().equals(firebaseAuth.getUid())) {
                        role = user.getRole();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (role.equals("user")){
            binding.buttonAddEvent.setVisibility(View.GONE);
        }

        binding.buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика добавления нового события
                addNewEvent();
            }
        });

        getEventsFromDatabase();

        return binding.getRoot();
    }

    private void addNewEvent() {
        AddEventFragment addEventFragment = new AddEventFragment();
        ((MainActivity) getActivity()).startFragment(addEventFragment);
    }

    public void getEventsFromDatabase(){
        eventList.clear();
        final boolean[] flag = {false};
        Log.d("eventPage", "getEventsFromDatabase eventList: " + eventList.size());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(EVENT_KEY);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (flag[0]){
                    return;
                }
                flag[0] = true;
                Log.d("eventPage", "onDataChange Start receiving events");
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Event event = ds.getValue(Event.class);
                    Log.d("eventPage", "Getting event " + event.getTitle());
                    eventList.add(event);
                }
                setEventListView();
                Log.d("eventPage", "Items:" + eventList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("eventPage", "Events receiving failed");
            }
        });
    }
    
    public void setEventListView() {
        eventListAdapter = new EventListAdapter(getContext(), eventList);
        binding.eventList.setAdapter(eventListAdapter);
    }
}