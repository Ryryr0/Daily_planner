package com.example.dailyplanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.R;
import com.example.dailyplanner.databinding.ActivityInformationBinding;
import com.example.dailyplanner.informationPages.AboutAuthorFragment;
import com.example.dailyplanner.informationPages.AboutProgramFragment;
import com.example.dailyplanner.informationPages.UserInstructionFragment;

public class InformationActivity extends AppCompatActivity {

    private ActivityInformationBinding binding;
    private AboutProgramFragment aboutProgramFragment;
    private AboutAuthorFragment aboutAuthorFragment;
    private UserInstructionFragment userInstructionFragment;
    private final int CONTAINER_VIEW_ID = 123123123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fragmentContainer.setId(CONTAINER_VIEW_ID);
        aboutAuthorFragment = new AboutAuthorFragment();
        aboutProgramFragment = new AboutProgramFragment();
        userInstructionFragment = new UserInstructionFragment();

        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTAINER_VIEW_ID, aboutProgramFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu_for_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.navigation_about_author) {
            selectedFragment = aboutAuthorFragment;
        } else if (item.getItemId() == R.id.navigation_about_program) {
            selectedFragment = aboutProgramFragment;
        } else if (item.getItemId() == R.id.navigation_instruction) {
            selectedFragment = userInstructionFragment;
        } else if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        // Заменяем текущий фрагмент на выбранный;
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTAINER_VIEW_ID, selectedFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();

        return true;
    }
}