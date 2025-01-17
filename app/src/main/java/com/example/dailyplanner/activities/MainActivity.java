package com.example.dailyplanner.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.R;
import com.example.dailyplanner.databinding.ActivityMainBinding;
import com.example.dailyplanner.mainpages.eventpage.EventPageFragment;
import com.example.dailyplanner.mainpages.profile.ProfileFragment;
import com.example.dailyplanner.mainpages.taskpage.CreateTaskFragment;
import com.example.dailyplanner.mainpages.taskpage.TasksPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements TasksPageFragment.OnCreateNewTask {
    private ActivityMainBinding binding;
    private TasksPageFragment taskPageFragment;
    private EventPageFragment eventPageFragment;
    private ProfileFragment profileFragment;
    private final int CONTAINER_VIEW_ID = 19191919;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fragmentContainer.setId(CONTAINER_VIEW_ID);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(navListener);
        taskPageFragment = new TasksPageFragment();
        eventPageFragment = new EventPageFragment();
        profileFragment = new ProfileFragment();

        // Устанавливаем фрагмент по умолчанию
        getSupportFragmentManager().beginTransaction().replace(CONTAINER_VIEW_ID,
                taskPageFragment).commit();
        binding.bottomNavigation.setSelectedItemId(R.id.navigation_tasks);
    }

    // Слушатель для навигационной панели
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.navigation_tasks)
                            selectedFragment = taskPageFragment;
                    else if (item.getItemId() == R.id.navigation_events)
                            selectedFragment = eventPageFragment;
                    else if (item.getItemId() == R.id.navigation_profile)
                            selectedFragment = profileFragment;

                    // Заменяем текущий фрагмент на выбранный;
                    FragmentTransaction fTrans;
                    fTrans = getSupportFragmentManager().beginTransaction();
                    fTrans.replace(CONTAINER_VIEW_ID, selectedFragment);
                    fTrans.addToBackStack(null);
                    fTrans.commit();

                    return true;
                }
    };

    @Override
    public void onCreateNewTask(int year, int month, int dayOfMonth) {
        CreateTaskFragment newFragment = new CreateTaskFragment(year, month, dayOfMonth);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(CONTAINER_VIEW_ID, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(CONTAINER_VIEW_ID, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}