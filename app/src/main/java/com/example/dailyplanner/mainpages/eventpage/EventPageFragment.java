package com.example.dailyplanner.mainpages.eventpage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyplanner.activities.MainActivity;
import com.example.dailyplanner.anxiliary.Event;
import com.example.dailyplanner.anxiliary.EventListAdapter;
import com.example.dailyplanner.databinding.FragmentEventPageBinding;

import java.util.ArrayList;
import java.util.List;

public class EventPageFragment extends Fragment implements EventListAdapter.OnEventClickListener {

    FragmentEventPageBinding binding;
    private RecyclerView recyclerView;
    private EventListAdapter eventListAdapter;
    private List<Event> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEventPageBinding.inflate(inflater, container, false);

        binding.buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика добавления нового события
                addNewEvent();
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList = new ArrayList<>();
        eventListAdapter = new EventListAdapter(eventList, this);
        recyclerView.setAdapter(eventListAdapter);

        return binding.getRoot();
    }

    private void addNewEvent() {
        AddEventFragment addEventFragment = new AddEventFragment();
        addEventFragment.setOnEventCreatedListener(new AddEventFragment.OnEventCreatedListener() {
            @Override
            public void onEventCreated(Event event) {
                Log.d("myTag", "Creating new event element");
                eventList.add(event);
                eventListAdapter.notifyItemInserted(eventList.size() - 1);
            }
        });

        ((MainActivity) getActivity()).startFragment(addEventFragment);
    }

    @Override
    public void onAddToTaskClick(int position) {
        // Логика добавления события в список задач
    }

    @Override
    public void onDeleteClick(int position) {
        // Логика удаления события
        eventList.remove(position);
        eventListAdapter.notifyItemRemoved(position);
    }
}