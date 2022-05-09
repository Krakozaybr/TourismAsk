package com.krak.tourismask.main_activity.fragments.sights;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.krak.tourismask.databinding.SightsFragmentBinding;
import com.krak.tourismask.entities.Sight;
import com.krak.tourismask.sight_activities.NewSightActivity;
import com.krak.tourismask.utils.CustomAnimations;
import com.krak.tourismask.utils.PreferenceManager;
import com.krak.tourismask.view_models.SightsViewModel;

import java.util.ArrayList;
import java.util.List;


public class SightsFragment extends Fragment {

    private static final String TAG = "InfoFragment";

    private SightsFragmentBinding binding;
    private ViewModelProvider provider;
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        provider = new ViewModelProvider(getActivity());
        binding = SightsFragmentBinding.inflate(getLayoutInflater());
        initManagers();
        addEventListeners();
        addObservers();
        checkAdvice();
        return binding.getRoot();
    }

    private void initManagers() {
        preferenceManager = new PreferenceManager((AppCompatActivity) getActivity());
    }

    // Проверяем включены ли подсказки
    private void checkAdvice() {
        if (preferenceManager.getAdvices()){
            binding.adviceLayout.setVisibility(View.VISIBLE);
        }
    }

    private void addEventListeners(){
        // Пользователь нажал на заголовок "Встроенные места"
        binding.includeSightsTitleLayout.setOnClickListener(view -> {
            CustomAnimations.toggleVisibility(binding.includeSightsContentLayout, binding.includeSightsTriangle);
        });
        // Пользователь нажал на заголовок "Ваши места"
        binding.userSightsTitleLayout.setOnClickListener(view -> {
            CustomAnimations.toggleVisibility(binding.userSightsContentLayout, binding.userSightsTriangle);
        });
        // Пользователь нажал на кнопку "Добавить достопримечательность"
        binding.addSightBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), NewSightActivity.class);
            startActivity(intent);
        });
        // Пользователь нажал на крестик для того, чтобы закрыть подсказку
        binding.adviceCross.setOnClickListener(view -> {
            CustomAnimations.toggleVisibility(binding.adviceLayout);
        });
    }

    private void addObservers(){
        // Следим за обновлениями моделей Sight
        provider.get(SightsViewModel.class).getData().observe(getActivity(), this::updateSights);
    }

    // Обновляем списки
    private void updateSights(List<Sight> sights){
        if (getActivity() == null){
            return;
        }
        List<Sight> include = new ArrayList<>();
        List<Sight> user = new ArrayList<>();
        for (Sight sight : sights){
            if (sight.getId() == -1){
                include.add(sight);
            } else {
                user.add(sight);
            }
        }
        binding.includeSightsList.setAdapter(new SightsAdapter(getActivity(), include));
        // Если нет пользовательских мест, то делаем невидимым ConstraintLayout
        // их содержащий, в ином случае добавляем адаптер и делаем видимым родителя
        if (user.isEmpty()){
            binding.userSightsListLayout.setVisibility(View.GONE);
        } else {
            binding.userSightsListLayout.setVisibility(View.VISIBLE);
            binding.userSightsListLayout.bringToFront();
            binding.userSightsList.setAdapter(new SightsAdapter(getActivity(), user));
        }
    }
}
