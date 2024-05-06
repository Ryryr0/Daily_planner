package com.example.dailyplanner;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.databinding.FragmentRegistrationBinding;

public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;
    private RegistrationListener registrationListener;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateUserData()) {
                    String lastName = binding.editTextLastName.getText().toString();
                    String firstName = binding.editTextFirstName.getText().toString();
                    String password = binding.editTextPassword.getText().toString();

                    User user = new User(lastName, firstName, password);
                    registrationListener.onRegistrationComplete(user);

                    // Закрытие фрагмента
                    //getParentFragmentManager().popBackStack();
                }
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrationListener.onRegistrationEntrance();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            registrationListener = (RegistrationListener) context;
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
        String lastName = binding.editTextLastName.getText().toString();
        String firstName = binding.editTextFirstName.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(lastName) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            // Проверка на пустые поля
            Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            // Проверка на совпадение паролей
            Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public interface RegistrationListener {
        void onRegistrationComplete(User user);
        void onRegistrationEntrance();
    }
}
