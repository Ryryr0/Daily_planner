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
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.dailyplanner.activities.MainActivity;
import com.example.dailyplanner.anxiliary.Task;
import com.example.dailyplanner.anxiliary.Types;
import com.example.dailyplanner.databinding.FragmentCreateTaskBinding;

public class CreateTaskFragment extends Fragment {

    FragmentCreateTaskBinding binding;
    private Spinner spinnerType;
    Task task;

    public CreateTaskFragment(int year, int month, int dayOfMonth){
        task = new Task("name", "description", Types.OTHER, year, month, dayOfMonth, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);

        // Настройка верхней панели
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true); // Показать кнопку "назад"
                actionBar.setTitle("Создать задачу"); // Установить заголовок
            }
        }

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

                if (name.isEmpty() && description.isEmpty()) {
                    task.setName(name);
                    task.setDescription(description);
                    task.setType(type);
                    ((OnCreateTask) getActivity()).onCreateTask(task);
                }
                else {
                    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
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

    public interface OnCreateTask{
        void onCreateTask(Task task);
    }
}