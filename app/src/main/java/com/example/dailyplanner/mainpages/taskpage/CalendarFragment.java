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
    OnCalendarListener calendarListener;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        calendarListener = (OnCalendarListener) getParentFragment();

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {
                // Something do
                calendarListener.onCalendarSelected(year, month, dayOfMonth);
            }
        });

        return view;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof OnCalendarListener) {
//            calendarListener = (OnCalendarListener) context;
//        } else {
//            throw new RuntimeException(context.toString() +
//                    " must implement OnInnerFragmentInteractionListener");
//        }
//    }

    public interface OnCalendarListener {
        public void onCalendarSelected(int year, int month, int dayOfMonth);
    }
}