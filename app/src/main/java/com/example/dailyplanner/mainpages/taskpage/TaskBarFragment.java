package com.example.dailyplanner.mainpages.taskpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailyplanner.R;
import com.example.dailyplanner.anxiliary.TaskListAdapter;
import com.example.dailyplanner.databinding.FragmentTaskBarBinding;

import java.util.ArrayList;

public class TaskBarFragment extends Fragment implements TasksPageFragment.OnTaskPageListener {

    private FragmentTaskBarBinding binding;

    ArrayList<Task> tasks = new ArrayList<>();
    TaskListAdapter taskListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaskBarBinding.inflate(inflater, container, false);

        fillData();
        taskListAdapter = new TaskListAdapter(this.getContext(), tasks);
        binding.listViewTasks.setAdapter(taskListAdapter);

        return binding.getRoot();
    }

    public void fillData() {
        for (int i = 0; i < 6; i++) {
            tasks.add(new Task());
        }
    }

    @Override
    public void onTaskPageCreate(int year, int month, int dayOfMonth, String dayOfWeek) {

    }
}