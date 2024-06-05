package com.example.dailyplanner.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;

public class RegistrationActivity extends AppCompatActivity
        implements RegistrationFragment.RegistrationListener, EntranceFragment.EntranceListener {
    private ActivityRegistrationBinding binding;
    private RegistrationFragment registrationFragment;
    private EntranceFragment entranceFragment;
    private static final int CONTENT_VIEW_ID = 10101010;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private final String MY_TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fragmentContainerView.setId(CONTENT_VIEW_ID);
        registrationFragment = new RegistrationFragment();
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();

        String userJson = sharedPreferences.getString("user", null);
        if (userJson != null) {
            User savedUser = gson.fromJson(userJson, User.class);
            entranceFragment = new EntranceFragment(savedUser);
            Log.d(MY_TAG, "Saved user authorised");
        }
        else {
            entranceFragment = new EntranceFragment();
        }

        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(CONTENT_VIEW_ID, entranceFragment);
        fTrans.commit();

        Log.d(MY_TAG, "EntranceFragment transaction was completed");
    }

    @Override
    public void onRegistrationComplete(User user) {
        String userJson;
        if (user.isRememberMe()) {
            userJson = gson.toJson(user);
            editor.putString("user", userJson);
            editor.apply();
        } else {
            // Очищаем данные пользователя
            editor.clear();
            editor.apply();
        }
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEntranceComplete(User user) {
        String userJson;
        if (user.isRememberMe()) {
            userJson = gson.toJson(user);
            editor.putString("user", userJson);
            editor.apply();
        } else {
            // Очищаем данные пользователя
            editor.clear();
            editor.apply();
        }
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistrationEntrance() {
        // Replacing RegistrationFragment by EntranceFragment
        FragmentTransaction fTrans;
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(CONTENT_VIEW_ID, entranceFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
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

    private void saveAuthorization() {

    }
}