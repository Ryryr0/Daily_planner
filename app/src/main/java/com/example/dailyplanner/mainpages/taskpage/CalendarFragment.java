package com.example.dailyplanner.mainpages.taskpage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.dailyplanner.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    FragmentCalendarBinding binding;
//    OnSomeEventListener calendarListener;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {
                // Something do
            }
        });

        return view;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        try {
//            calendarListener = (OnSomeEventListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    " must implement OnSomeEventListener");
//        }
//    }
}