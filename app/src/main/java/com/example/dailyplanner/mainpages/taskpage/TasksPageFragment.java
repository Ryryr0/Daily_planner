package com.example.dailyplanner.mainpages.taskpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dailyplanner.R;
import com.example.dailyplanner.anxiliary.TaskListAdapter;
import com.example.dailyplanner.databinding.FragmentTasksPageBinding;

import java.util.ArrayList;

public class TasksPageFragment extends Fragment implements CalendarFragment.OnCalendarListener {
    private FragmentTasksPageBinding binding;
    private CalendarFragment calendarFragment;
    private final int CONTAINER_VIEW_ID = 23232323;
    private final int CONTAINER_LIST_VIEW_ID = 34343434;
    private boolean buttonCalendarFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTasksPageBinding.inflate(inflater, container, false);

        binding.fragmentContainerCalendar.setId(CONTAINER_VIEW_ID);
        binding.fragmentContainerTaskPanel.setId(CONTAINER_LIST_VIEW_ID);
        calendarFragment = new CalendarFragment();

        FragmentTransaction fTrans;
        fTrans = getChildFragmentManager().beginTransaction();
        fTrans.replace(CONTAINER_LIST_VIEW_ID, new TaskBarFragment());
        fTrans.addToBackStack(null);
        fTrans.commit();

        binding.buttonOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonCalendarFlag) {
                    FragmentTransaction fTrans;
                    fTrans = getChildFragmentManager().beginTransaction();
                    fTrans.add(CONTAINER_VIEW_ID, calendarFragment);
                    fTrans.addToBackStack(null);
                    fTrans.commit();
                }
                else {
                    FragmentTransaction fTrans;
                    fTrans = getChildFragmentManager().beginTransaction();
                    fTrans.remove(calendarFragment);
                    fTrans.addToBackStack(null);
                    fTrans.commit();
                }
                buttonCalendarFlag = !buttonCalendarFlag;
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentTransaction fTrans;
        fTrans = getChildFragmentManager().beginTransaction();
        fTrans.remove(calendarFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
        buttonCalendarFlag = false;
    }

    @Override
    public void onCalendarSelected(int year, int month, int dayOfMonth, String dayOfWeek) {

    }
}