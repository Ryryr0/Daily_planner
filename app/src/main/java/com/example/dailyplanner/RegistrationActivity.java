package com.example.dailyplanner;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.databinding.ActivityMainBinding;

public class RegistrationActivity extends AppCompatActivity
        implements RegistrationFragment.RegistrationListener, EntranceFragment.EntranceListener {
    private ActivityMainBinding binding;
    private RegistrationFragment registrationFragment;
    private EntranceFragment entranceFragment;
    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Creating registration fragment
        binding.fragmentContainerView.setId(CONTENT_VIEW_ID);
        registrationFragment = new RegistrationFragment();
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(CONTENT_VIEW_ID, registrationFragment);
        fTrans.commit();
    }

    @Override
    public void onRegistrationComplete(User user) {
        Toast.makeText(this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationEntrance() {
        // Replacing RegistrationFragment by EntranceFragment
        entranceFragment = new EntranceFragment();
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTENT_VIEW_ID, entranceFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    @Override
    public void onEntranceComplete(User user) {
        Toast.makeText(this, "Вы вошли!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEntranceRegistration() {
        // Replacing EntranceFragment by RegistrationFragment
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTENT_VIEW_ID, registrationFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }
}