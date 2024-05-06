package com.example.dailyplanner;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements RegistrationFragment.RegistrationListener {
    private ActivityMainBinding binding;
    private RegistrationFragment registrationFragment;
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


    public void onRegistrationComplete(User user) {

    }

    public void onRegistrationEntrance() {

    }
}