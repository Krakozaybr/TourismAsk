package com.krak.tourismask.sight_activities;

import android.os.Bundle;

import com.krak.tourismask.R;
import com.krak.tourismask.entities.Sight;

public class EditSightActivity extends NewSightActivity {

    public static final String SIGHT = "SIGHT";
    private Sight sight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIntent();
        // Возвращаемся к SightsFragment
        comeBackId = R.id.nav_sights;
        // Устанавливаем id, чтобы логика NewSightActivity обрабатывала
        // вводные как редактирование
        id = sight.getId();
    }

    private void checkIntent(){
        // В Intent нам должны пересылать Sight для изменения
        // Загружаем его информацию в поля для ввода
        sight = Sight.parse(getIntent().getStringExtra(SIGHT));
        binding.sightTitleInput.setText(sight.getTitle());
        binding.sightDescriptionInput.setText(sight.getDescription());
        binding.sightLatitudeInput.setText(sight.getLatitude() + "");
        binding.sightLongitudeInput.setText(sight.getLongitude() + "");
    }
}
