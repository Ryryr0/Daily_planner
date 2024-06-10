package com.example.dailyplanner.mainpages.taskpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.anxiliary.Task;
import com.example.dailyplanner.anxiliary.Types;
import com.example.dailyplanner.databinding.FragmentCreateTaskBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTaskFragment extends Fragment {

    FragmentCreateTaskBinding binding;
    private Spinner spinnerType;
    private Task task;
    private final String TASK_KEY = "Tasks";

    public CreateTaskFragment(int year, int month, int dayOfMonth){
        task = new Task("name", "description", Types.OTHER, year, month, dayOfMonth, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);

        // Настройка селектора для типа задачи
        spinnerType = binding.spinnerType;
        setupTypeSelector();

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, description, strType;
                Types type = Types.OTHER;
                name = String.valueOf(binding.editTextName.getText());
                description = String.valueOf(binding.editTextDescription.getText());

                strType = binding.spinnerType.getSelectedItem().toString();
                switch (strType){
                    case "Работа":
                        type = Types.WORK;
                        break;
                    case "Отдых":
                        type = Types.REST;
                        break;
                    case "Рутина":
                        type = Types.ROUTINE;
                        break;
                    case "Другое":
                        type = Types.OTHER;
                        break;
                }

                if (!name.isEmpty() && !description.isEmpty()) {
                    task.setName(name);
                    task.setDescription(description);
                    task.setType(type);
                    saveTask();
                }
                else {
                    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
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

    // Метод для настройки селектора типа задачи
    private void setupTypeSelector() {
        // Массив типов задач
        String[] types = {"Работа", "Отдых", "Рутина", "Другое"};

        // Адаптер для селектора
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Установка адаптера на селектор
        spinnerType.setAdapter(adapter);
    }

    // Обработка нажатия кнопки "назад"
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed(); // Вернуться назад при нажатии кнопки "назад"
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveTask(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(TASK_KEY);
        task.setUserId(firebaseAuth.getUid());
        databaseReference.push().setValue(task);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public interface OnCreateTask{
        void onCreateTask(Task task);
    }
}