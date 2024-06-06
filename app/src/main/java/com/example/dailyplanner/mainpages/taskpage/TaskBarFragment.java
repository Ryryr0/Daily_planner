package com.example.dailyplanner.mainpages.taskpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailyplanner.anxiliary.Task;
import com.example.dailyplanner.anxiliary.TaskListAdapter;
import com.example.dailyplanner.databinding.FragmentTaskBarBinding;

import java.util.ArrayList;

public class TaskBarFragment extends Fragment {

    private FragmentTaskBarBinding binding;
    ArrayList<Task> tasks = new ArrayList<>();
    TaskListAdapter taskListAdapter;
    String date;

    public TaskBarFragment(String date, ArrayList<Task> tasks) {
        this.date = date;
        this.tasks = tasks;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaskBarBinding.inflate(inflater, container, false);

        binding.textViewHeading.setText(date);
        taskListAdapter = new TaskListAdapter(this.getContext(), tasks);
        binding.listViewTasks.setAdapter(taskListAdapter);

        return binding.getRoot();
    }
}