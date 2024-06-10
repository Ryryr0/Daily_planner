package com.example.dailyplanner.mainpages.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dailyplanner.activities.InformationActivity;
import com.example.dailyplanner.activities.RegistrationActivity;
import com.example.dailyplanner.anxiliary.User;
import com.example.dailyplanner.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final int UCROP_REQUEST_CODE = UCrop.REQUEST_CROP;
    private Uri currentImageUri; // Сохраняем текущий Uri изображения
    private int k = 0;
    private final String MY_TAG = "myTag";
    private final String USER_KEY = "Users";
    private User user = new User();
    private String userRecordKey;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        currentImageUri = imageUri; // Сохраняем новый Uri изображения
                        startCrop(currentImageUri);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        loadingUserData();
        binding.editPhotoButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        binding.saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangedData();
            }
        });

        binding.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
        });

        binding.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

    private void startCrop(@Nullable Uri uri) {
        if (uri == null) {
            return;
        }

        String destinationFileName = "croppedImage" + k + ".png";
        k++;
        Uri destinationUri = Uri.fromFile(new File(requireActivity().getCacheDir(), destinationFileName));

        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(requireContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == UCROP_REQUEST_CODE) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    currentImageUri = resultUri; // Обновляем текущий Uri изображения
                    binding.avatarImageView.setImageURI(resultUri);
                    uploadImageToStorage(currentImageUri);
                    Log.d(MY_TAG, "New photo was set");
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            // Handle the error
        }
    }

    private void saveChangedData(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(USER_KEY);
        String uId = firebaseAuth.getUid();
        FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), user.getPassword());
        fireUser.reauthenticate(credential);

        user.setEmail(binding.emailEditText.getText().toString());
        user.setFirstName(binding.fullNameEditText.getText().toString());
        user.setPassword(binding.passwordEditText.getText().toString());

        fireUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                        fUser.updatePassword(user.getPassword())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("profileLog", "password was changed");
                                        }
                                    }
                                });
                    }
                });
        databaseReference.child(userRecordKey).setValue(user);
        Toast.makeText(getActivity(), "Data was saved", Toast.LENGTH_SHORT).show();
    }

    private void loadingUserData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(USER_KEY);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("profileLog", "Start loading data");
                for (DataSnapshot ds: snapshot.getChildren()) {
                    userRecordKey = ds.getKey();
                    user = ds.getValue(User.class);
                    if (user.getId().equals(firebaseAuth.getUid())) {
                        binding.fullNameEditText.setText(user.getFirstName());
                        binding.emailEditText.setText(user.getEmail());
                        binding.passwordEditText.setText(user.getPassword());
                        downloadImageFromStorage();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void uploadImageToStorage(Uri imageUri) {
        String fileName = "user_icon_" + user.getId() + ".png";

        // Получаем ссылку на Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Создаем ссылку на файл в Firebase Storage
        StorageReference imageRef = storageRef.child("Users_profile_icons/" + fileName);

        // Загружаем файл в Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("profileLog", "Изображение успешно загружено в Firebase Storage.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("profileLog", "Ошибка при загрузке изображения в Firebase Storage: ", exception);
                    }
                });
    }

    public void downloadImageFromStorage() {
        String fileName = "user_icon_" + user.getId() + ".png";

        // Получаем ссылку на Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Создаем ссылку на файл в Firebase Storage gs://daily-planer-2.appspot.com/Users_profile_icons
        StorageReference imageRef = storageRef.child("Users_profile_icons/" + fileName);
        Log.d("profileLog", imageRef.getPath().toString());

        // Получаем URL для скачивания
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Используем Glide для загрузки изображения по URI
                Glide.with(getContext())
                        .load(uri)
                        .into(binding.avatarImageView);
                Log.d("profileLog", "URI изображения успешно получен и загружен в ImageView.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("profileLog", "Ошибка при получении URI изображения из Firebase Storage: ", exception);
            }
        });
    }
}