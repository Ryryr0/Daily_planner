package com.example.dailyplanner.mainpages.eventpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.R;
import com.example.dailyplanner.anxiliary.Event;
import com.example.dailyplanner.databinding.FragmentAddEventBinding;

public class AddEventFragment extends Fragment {

    FragmentAddEventBinding binding;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private ImageView imageView;
    private Button buttonAddEvent;
    private Button buttonCancel;
    private int imageResId = R.drawable.my_ava; // пример изображения, это можно сделать более гибким

    public interface OnEventCreatedListener {
        void onEventCreated(Event event);
    }

    private OnEventCreatedListener onEventCreatedListener;

    public void setOnEventCreatedListener(OnEventCreatedListener listener) {
        this.onEventCreatedListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);

        editTextTitle = binding.editTextTitle;
        editTextDescription = binding.editTextDescription;
        imageView = binding.imageView;
        buttonAddEvent = binding.buttonAddEvent;
        buttonCancel = binding.buttonCancel;

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                Event newEvent = new Event(imageResId, title, description);
                if (onEventCreatedListener != null) {
                    onEventCreatedListener.onEventCreated(newEvent);
                }
                getParentFragmentManager().popBackStack();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }
}