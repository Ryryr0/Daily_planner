package com.example.dailyplanner.mainpages.eventpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.anxiliary.Event;
import com.example.dailyplanner.databinding.FragmentAddEventBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class AddEventFragment extends Fragment {

    FragmentAddEventBinding binding;
    private Uri currentImageUri;
    private String imagePath;
    private final String EVENT_KEY = "Events";
    int k = 0;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        currentImageUri = imageUri; // Сохраняем новый Uri изображения
                        binding.imageView.setImageURI(currentImageUri);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container, false);

        binding.buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.editTextTitle.getText().toString();
                String description = binding.editTextDescription.getText().toString();
                int year = Integer.parseInt(binding.editTextYear.getText().toString());
                int mont = Integer.parseInt(binding.editTextMonth.getText().toString());
                int dayOfMonth = Integer.parseInt(binding.editTextDayOfMonth.getText().toString());
                uploadImageToStorage(dayOfMonth, mont, year);
                Event newEvent = new Event(imagePath, title, description, mont, dayOfMonth, year);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference(EVENT_KEY);
                databaseReference.push().setValue(newEvent);

                getParentFragmentManager().popBackStack();
            }
        });

        binding.buttonAddImageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    public void uploadImageToStorage(int dayOfMonth, int month, int year) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String fileName = "event_image_" + firebaseAuth.getCurrentUser().getUid() + getRandomInt()
                + dayOfMonth + month + year + ".png";
        imagePath = fileName;

        // Получаем ссылку на Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Создаем ссылку на файл в Firebase Storage
        StorageReference imageRef = storageRef.child("Event_images/" + fileName);

        // Загружаем файл в Firebase Storage
        imageRef.putFile(currentImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("eventPage", "Изображение успешно загружено в Firebase Storage.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("eventPage", "Ошибка при загрузке изображения в Firebase Storage: ", exception);
                    }
                });
    }

    public int getRandomInt() {
        Random random = new Random();
        int min = 1;
        int max = 10000000;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return randomNumber;
    }
}