package com.example.dailyplanner.mainpages.taskpage;

import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dailyplanner.R;
import com.example.dailyplanner.anxiliary.Task;
import com.example.dailyplanner.anxiliary.TaskListAdapter;
import com.example.dailyplanner.databinding.FragmentTasksPageBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TasksPageFragment extends Fragment implements CalendarFragment.OnCalendarListener {
    private FragmentTasksPageBinding binding;
    private CalendarFragment calendarFragment;
    private final int CONTAINER_VIEW_ID = 23232323;
    private final int CONTAINER_LIST_VIEW_ID = 34343434;
    private boolean buttonCalendarFlag = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTasksPageBinding.inflate(inflater, container, false);

        binding.fragmentContainerCalendar.setId(CONTAINER_VIEW_ID);
        binding.fragmentContainerTaskPanel.setId(CONTAINER_LIST_VIEW_ID);
        calendarFragment = new CalendarFragment();

        setCurrentDate();

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
        createTaskBarFragment(year, month, dayOfMonth, dayOfWeek);
    }

    public void setCurrentDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            String dayOfWeek = "";
            switch (localDateTime.getDayOfWeek()) {
                case SUNDAY:
                    dayOfWeek = "Вс";
                    break;
                case MONDAY:
                    dayOfWeek = "Пн";
                    break;
                case TUESDAY:
                    dayOfWeek = "Вт";
                    break;
                case WEDNESDAY:
                    dayOfWeek = "Ср";
                    break;
                case THURSDAY:
                    dayOfWeek = "Чт";
                    break;
                case FRIDAY:
                    dayOfWeek = "Пт";
                    break;
                case SATURDAY:
                    dayOfWeek = "Сб";
                    break;
            }
            localDateTime = LocalDateTime.now();
            createTaskBarFragment(
                    localDateTime.getYear(),
                    localDateTime.getMonthValue(),
                    localDateTime.getDayOfMonth(),
                    dayOfWeek);
        }
    }

    private void createTaskBarFragment(int year, int month, int dayOfMonth, String dayOfWeek) {
        String strMonth = "";
        switch (month) {
            case 1:
                strMonth = "Январь";
                break;
            case 2:
                strMonth = "Февраль";
                break;
            case 3:
                strMonth = "Март";
                break;
            case 4:
                strMonth = "Апрель";
                break;
            case 5:
                strMonth = "Май";
                break;
            case 6:
                strMonth = "Июнь";
                break;
            case 7:
                strMonth = "Июль";
                break;
            case 8:
                strMonth = "Август";
                break;
            case 9:
                strMonth = "Сентябрь";
                break;
            case 10:
                strMonth = "Октябрь";
                break;
            case 11:
                strMonth = "Ноябрь";
                break;
            case 12:
                strMonth = "Декабрь";
                break;

        }
        String date = strMonth + "\n" + dayOfWeek + "\n" + dayOfMonth;

        // Take tasks from dataBase
        ArrayList<Task> taskList = new ArrayList<>();

        FragmentTransaction fTrans;
        fTrans = getChildFragmentManager().beginTransaction();
        fTrans.replace(CONTAINER_LIST_VIEW_ID,
                new TaskBarFragment(date, taskList));
        fTrans.addToBackStack(null);
        fTrans.commit();
    }
}