package com.example.dailyplanner.mainpages.taskpage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailyplanner.R;
import com.example.dailyplanner.databinding.FragmentTasksPageBinding;

public class TasksPageFragment extends Fragment {
    private FragmentTasksPageBinding binding;
    private CalendarFragment calendarFragment;
    private final int CONTAINER_VIEW_ID = 23232323;
    private boolean buttonCalendarFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTasksPageBinding.inflate(inflater, container, false);

        binding.fragmentContainer.setId(CONTAINER_VIEW_ID);
        calendarFragment = new CalendarFragment();

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

}