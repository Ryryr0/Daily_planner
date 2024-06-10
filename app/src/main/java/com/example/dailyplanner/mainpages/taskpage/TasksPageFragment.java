package com.example.dailyplanner.mainpages.taskpage;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.R;
import com.example.dailyplanner.anxiliary.Task;
import com.example.dailyplanner.anxiliary.TaskListAdapter;
import com.example.dailyplanner.databinding.FragmentTasksPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TasksPageFragment extends Fragment implements CalendarFragment.OnCalendarListener {
    private FragmentTasksPageBinding binding;
    private CalendarFragment calendarFragment;
    private final int CONTAINER_VIEW_ID = 23232323;
    private final int CONTAINER_LIST_VIEW_ID = 34343434;
    private boolean buttonCalendarFlag = false;
    private int year, month, dayOfMonth;
    private String date;
    private String dayOfWeek;
    TaskListAdapter taskListAdapter;
    private final String TASK_KEY = "Tasks";
    private Context myContext;
    ArrayList<Task> taskArrayList = new ArrayList<>();
    private boolean deleteModeFlag = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTasksPageBinding.inflate(inflater, container, false);

        binding.fragmentContainerCalendar.setId(CONTAINER_VIEW_ID);
//        taskListAdapter = new TaskListAdapter(getContext(), taskArrayList);
        calendarFragment = new CalendarFragment();
        FragmentTransaction fTrans;
        fTrans = getChildFragmentManager().beginTransaction();
        fTrans.add(CONTAINER_VIEW_ID, calendarFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
        binding.fragmentContainerCalendar.setVisibility(View.GONE);

        setCurrentDate();
        createTaskBarFragment(year, month, dayOfMonth, dayOfWeek);

        binding.buttonOpenCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonCalendarFlag) {
                    binding.fragmentContainerCalendar.setVisibility(View.VISIBLE);
                }
                else {
                    binding.fragmentContainerCalendar.setVisibility(View.GONE);
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

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < binding.listViewTasks.getChildCount(); i++) {
                    // Получаем вид каждого элемента
                    View listItemView = binding.listViewTasks.getChildAt(i);

                    // Находим кнопку в каждом элементе списка
                    Button button = listItemView.findViewById(R.id.buttonItemDelete); // Здесь R.id.button - это идентификатор кнопки в вашем элементе списка

                    // Если кнопка найдена, меняем ее видимость
                    if (button != null) {
                        if (button.getVisibility() == View.VISIBLE) {
                            button.setVisibility(View.GONE);
                        } else {
                            button.setVisibility(View.VISIBLE);
                        }
                    }
                }
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
        this.month = month + 1;
        this.dayOfMonth = dayOfMonth;
        createTaskBarFragment(year, month, dayOfMonth, dayOfWeek);
        Log.d("taskPage", "data was changed");
    }

    public void setCurrentDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate localDate = LocalDate.now();
            this.year = localDate.getYear();
            this.month = localDate.getMonthValue();
            this.dayOfMonth = localDate.getDayOfMonth();
            LocalDateTime localDateTime = LocalDateTime.now();
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
        date = strMonth + "\n" + dayOfWeek + "\n" + dayOfMonth;
        Log.d("taskPage", "OnCreateTaskBar");
        getTasksFromDatabase();
    }

    public void getTasksFromDatabase(){
        taskArrayList.clear();
        final boolean[] flag = {false};
        Log.d("taskPage", "getTasksFromDatabase taskArrayList: " + taskArrayList.size());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(TASK_KEY);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (flag[0]){
                    return;
                }
                flag[0] = true;
                Log.d("taskPage", "onDataChange Start receiving tasks");
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Task task = ds.getValue(Task.class);
                    if (task.getUserId().equals(firebaseAuth.getUid()) && task.getDayOfMonth() == dayOfMonth
                    && task.getMonth() == month && task.getYear() == year) {
                        Log.d("taskPage", "Getting task " + task.getName());
                        taskArrayList.add(task);
                    }
                }
                setTaskListView();
                Log.d("taskPag", "Date:" + year + " " + month + " " + dayOfMonth + " Items:" + taskListAdapter.getCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("taskPage", "Tasks receiving failed");
            }
        });
    }

    public interface OnCreateNewTask {
        void onCreateNewTask(int year, int month, int dayOfMonth);
    }

    public class Utility {
        public void setListViewHeightBasedOnItems(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    public void setTaskListView() {
        binding.textViewHeading.setText(date);
        taskListAdapter = new TaskListAdapter(getContext(), taskArrayList);
        binding.listViewTasks.setAdapter(taskListAdapter);
        new Utility().setListViewHeightBasedOnItems(binding.listViewTasks);
    }
}