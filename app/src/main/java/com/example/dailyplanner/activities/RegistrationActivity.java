package com.example.dailyplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.mainpages.EntranceFragment;
import com.example.dailyplanner.R;
import com.example.dailyplanner.mainpages.RegistrationFragment;
import com.example.dailyplanner.anxiliary.User;
import com.example.dailyplanner.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity
        implements RegistrationFragment.RegistrationListener, EntranceFragment.EntranceListener {
    private ActivityRegistrationBinding binding;
    private RegistrationFragment registrationFragment;
    private EntranceFragment entranceFragment;
    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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