package com.example.dailyplanner.mainpages;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.anxiliary.User;
import com.example.dailyplanner.databinding.FragmentEntranceBinding;

public class EntranceFragment extends Fragment {

    private FragmentEntranceBinding binding;
    private EntranceListener entranceListener;
    private User user;

    private final String MY_TAG = "myTag";

    public EntranceFragment() {
        // Required empty public constructor
    }
    public EntranceFragment(User user) {
        Log.d(MY_TAG, user.toString());
        this.user = user;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEntranceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (user != null) {
            binding.editTextEmail.setText(user.getEmail());
            binding.editTextPassword.setText(user.getPassword());
            binding.rememberMe.setChecked(true);
        }

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUserData()) {
                    String email = binding.editTextEmail.getText().toString();
                    String password = binding.editTextPassword.getText().toString();

                    User user = new User("qwert", password, email, binding.rememberMe.isChecked());
                    entranceListener.onEntranceComplete(user);

                    // Закрытие фрагмента
                    //getParentFragmentManager().popBackStack();
                }
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entranceListener.onEntranceRegistration();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            entranceListener = (EntranceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement OnSomeEventListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean validateUserData() {
        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            // Проверка на пустые поля
            Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public interface EntranceListener {
        void onEntranceComplete(User user);
        void onEntranceRegistration();
    }
}