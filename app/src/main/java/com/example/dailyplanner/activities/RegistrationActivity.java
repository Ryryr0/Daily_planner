package com.example.dailyplanner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.dailyplanner.anxiliary.User;
import com.example.dailyplanner.databinding.ActivityRegistrationBinding;
import com.example.dailyplanner.mainpages.EntranceFragment;
import com.example.dailyplanner.mainpages.RegistrationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference;
    private String USER_KEY = "Users";
    private Intent intent;
    private User currentUser;


    private final String MY_TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = new Intent(this, MainActivity.class);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(USER_KEY);
        currentUser = null;

//        DatabaseReference myRef = database.getReference("message");
//        Map<String, String> test = new HashMap<>();
//        test.put("qwer", "1234");
//        myRef.push().setValue(test);
//        myRef.setValue(test);

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
    }

    @Override
    public void onRegistrationComplete(User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(MY_TAG, user.toString());
                if (task.isSuccessful()){
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

                    String userId = firebaseAuth.getUid();
                    currentUser = new User(userId, user.getFirstName(), user.getPassword(), user.getEmail(), user.isRememberMe());

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Exception exception = task.getException();
                    String errorMessage = "Регистрация не прошла";

                    if (exception != null) {
                        if (exception instanceof FirebaseAuthWeakPasswordException) {
                            errorMessage = "Пароль слишком слабый.";
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            errorMessage = "Неверный формат email.";
                        } else if (exception instanceof FirebaseAuthUserCollisionException) {
                            errorMessage = "Пользователь с таким email уже существует.";
                        } else {
                            errorMessage = exception.getMessage();
                        }
                    }

                    Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onEntranceComplete(User user) {
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
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


                    FirebaseUser cUser = firebaseAuth.getCurrentUser();
                    if (cUser != null) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    User startUser = ds.getValue(User.class);
                                    startUser.setRememberMe(entranceFragment.getRememberMe());
                                    if (startUser.getId().equals(firebaseAuth.getUid())) {
                                        databaseReference.child(ds.getKey()).setValue(startUser);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Exception exception = task.getException();
                    String errorMessage = "Ошибка входа";

                    if (exception != null) {
                        if (exception instanceof FirebaseAuthInvalidUserException) {
                            // Пользователь с таким email не найден
                            errorMessage = "Пользователь с таким email не найден.";
                        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                            // Неверный пароль
                            errorMessage = "Неверный пароль.";
                        } else {
                            errorMessage = exception.getMessage();
                        }
                    }

                    Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });;
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

    @Override
    protected void onPause() {
        super.onPause();
        if (currentUser != null) {
            databaseReference.push().setValue(currentUser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser cUser = firebaseAuth.getCurrentUser();
        if (cUser != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        User startUser = ds.getValue(User.class);
                        if (startUser.getId().equals(firebaseAuth.getUid())) {
                            if (startUser.isRememberMe()){
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}