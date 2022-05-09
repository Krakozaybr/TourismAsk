package com.krak.tourismask.main_activity.fragments.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.krak.tourismask.R;
import com.krak.tourismask.databinding.SettingsFragmentBinding;
import com.krak.tourismask.name_activity.NameActivity;
import com.krak.tourismask.utils.PreferenceManager;

public class SettingsFragment extends Fragment {

    private SettingsFragmentBinding binding;
    private PreferenceManager preferenceManager;
    private boolean advicesVisible;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SettingsFragmentBinding.inflate(getLayoutInflater());
        addEventListeners();
        initManagers();
        initData();
        return binding.getRoot();
    }

    private void initData() {
        advicesVisible = preferenceManager.getAdvices();
        updateText();
    }

    private void initManagers() {
        preferenceManager = new PreferenceManager((AppCompatActivity) getActivity());
    }

    private void addEventListeners() {
        // Пользователь нажал "Изменить имя"
        binding.changeNameBtn.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), NameActivity.class));
        });
        // Пользователь нажал "Выключить подсказки" или "Включить подсказки"
        binding.toggleAdvicesBtn.setOnClickListener(view -> {
            advicesVisible = !advicesVisible;
            updateText();
        });
    }

    // Меняем текст в кнопке включить/выключить подсказки
    private void updateText() {
        if (advicesVisible){
            binding.toggleAdvicesBtn.setText(getString(R.string.switch_off_advices));
        } else {
            binding.toggleAdvicesBtn.setText(getString(R.string.switch_on_advices));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        preferenceManager.saveAdvices(advicesVisible);
    }
}
