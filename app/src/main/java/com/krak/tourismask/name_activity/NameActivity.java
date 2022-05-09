package com.krak.tourismask.name_activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.krak.tourismask.R;
import com.krak.tourismask.databinding.NameActivityBinding;
import com.krak.tourismask.dialogs.MessageDialog;
import com.krak.tourismask.main_activity.MainActivity;
import com.krak.tourismask.utils.InputValidator;
import com.krak.tourismask.utils.PreferenceManager;

public class NameActivity extends AppCompatActivity {

    private NameActivityBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NameActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initManagers();
        addEventListeners();
        setInitialData();
    }

    // На тот случай, если имя уже сохранено
    private void setInitialData() {
        binding.inputName.setText(preferenceManager.getName());
    }

    private void addEventListeners() {
        // Нажата кнопка "Пропустить"
        binding.skipNameBtn.setOnClickListener(view -> {
            if (preferenceManager.getName().isEmpty()) {
                preferenceManager.saveName(getString(R.string.defaultName));
            }
            goNext();
        });
        // Нажата кнопка "Продолжить"
        binding.continueNameBtn.setOnClickListener(view -> {
            String name = binding.inputName.getText().toString();
            // Проверяем имя на валидность
            if (InputValidator.isValidUserName(name)){
                preferenceManager.saveName(name);
                goNext();
            } else {
                // Показываем диалоговое окно о провале проверки
                MessageDialog dialog = new MessageDialog(getString(R.string.input_error), getString(R.string.incorrect_name_text));
                dialog.show(getSupportFragmentManager(), "custom");
            }
        });
    }

    // Идём в MainActivity
    private void goNext(){
        startActivity(new Intent(NameActivity.this, MainActivity.class));
    }

    // Инициализируем менеджеры
    private void initManagers() {
        preferenceManager = new PreferenceManager(this);
    }
}
