package com.example.dailyplanner.mainpages.taskpage;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.anxiliary.Task;
import com.example.dailyplanner.databinding.FragmentTasksPageBinding;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TasksPageFragment extends Fragment implements CalendarFragment.OnCalendarListener {
    private FragmentTasksPageBinding binding;
    private CalendarFragment calendarFragment;
    private final int CONTAINER_VIEW_ID = 23232323;
    private final int CONTAINER_LIST_VIEW_ID = 34343434;
    private boolean buttonCalendarFlag = false;
    private int year, month, dayOfMonth;
    ArrayList<Task> taskArrayList = new ArrayList<>();

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

        binding.createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnCreateNewTask) getActivity()).onCreateNewTask(year, month, dayOfMonth);
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
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
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
            case 0:
                strMonth = "Январь";
                break;
            case 1:
                strMonth = "Февраль";
                break;
            case 2:
                strMonth = "Март";
                break;
            case 3:
                strMonth = "Апрель";
                break;
            case 4:
                strMonth = "Май";
                break;
            case 5:
                strMonth = "Июнь";
                break;
            case 6:
                strMonth = "Июль";
                break;
            case 7:
                strMonth = "Август";
                break;
            case 8:
                strMonth = "Сентябрь";
                break;
            case 9:
                strMonth = "Октябрь";
                break;
            case 10:
                strMonth = "Ноябрь";
                break;
            case 11:
                strMonth = "Декабрь";
                break;

        }
        String date = strMonth + "\n" + dayOfWeek + "\n" + dayOfMonth;

        // Take tasks from dataBase
        //
        //
        //
        //
        //

        FragmentTransaction fTrans;
        fTrans = getChildFragmentManager().beginTransaction();
        fTrans.replace(CONTAINER_LIST_VIEW_ID,
                new TaskBarFragment(date, taskArrayList));
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    public interface OnCreateNewTask {
        void onCreateNewTask(int year, int month, int dayOfMonth);
    }
}