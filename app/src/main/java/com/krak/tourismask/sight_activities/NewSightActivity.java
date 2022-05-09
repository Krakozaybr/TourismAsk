package com.krak.tourismask.sight_activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.krak.tourismask.R;
import com.krak.tourismask.app.App;
import com.krak.tourismask.databinding.SightActivityBinding;
import com.krak.tourismask.dialogs.MessageDialog;
import com.krak.tourismask.entities.Sight;
import com.krak.tourismask.main_activity.MainActivity;
import com.krak.tourismask.utils.CoordinatesManager;
import com.krak.tourismask.utils.CustomAnimations;
import com.krak.tourismask.utils.InputValidator;
import com.krak.tourismask.utils.location_listeners.CoordinatesLocationListener;

public class NewSightActivity extends AppCompatActivity {

    CoordinatesManager coordinatesManager;
    SightActivityBinding binding;
    // При переходе в MainActivity идём в MapFragment
    int comeBackId = R.id.nav_sights;
    // При id = -1 мы создаем новую сущность Sight. Иначе - обновляем существующую
    long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SightActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEventListeners();
        initManagers();
    }

    void initManagers() {
        coordinatesManager = new CoordinatesManager(this, new CoordinatesLocationListener());
    }

    void addEventListeners(){
        // Пользователь изменил положение переключателей
        binding.sightRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.sightRadioCurrentLocation){
                coordinatesManager.connect();
                if (coordinatesManager.checkLocationPermission()){
                    coordinatesManager.init();
                }
            }
            // Переключаем видимость полей для ввода координат
            toggleInputs();
        });
        // Пользователь нажал на кнопку "Отменить"
        binding.sightCancelBtn.setOnClickListener(view -> comeBack());
        // Пользователь нажал на кнопку "Сохранить"
        binding.sightSaveBtn.setOnClickListener(view -> {
            // Проверяем на возможность сохранения
            if (areInputsCorrect() && save()){
                comeBack();
            } else if (
                    coordinatesManager.checkLocationPermission() &&
                    !coordinatesManager.areCoordsCorrect()
            ){
                // Случай, когда выбрано "Текущее местоположение", но координаты еще не получены
                Toast.makeText(this, R.string.coordinates_are_not_available_yet, Toast.LENGTH_SHORT).show();
            } else if (
                    binding.sightRadioCurrentLocation.isChecked() &&
                    !coordinatesManager.checkLocationPermission()
            ){
                // Случай отказа в предоставлении разрешения на геолокацию
                Toast.makeText(this, R.string.permissin_is_denied, Toast.LENGTH_SHORT).show();
                coordinatesManager.connect();
            } else {
                // Случай некорректного ввода
                new MessageDialog(
                        getString(R.string.incorrect_input),
                        getString(R.string.incorrect_input_text)
                ).show(getSupportFragmentManager(), "custom");
            }
        });
    }

    // Переключаем видимость полей для ввода координат
    void toggleInputs(){
        CustomAnimations.toggleVisibility(binding.sightLatitudeInput);
        CustomAnimations.toggleVisibility(binding.sightLongitudeInput);
    }

    // Проверяем ввод на корректность
    boolean areInputsCorrect(){
        return !binding.sightTitleInput.getText().toString().isEmpty() &&
                !binding.sightDescriptionInput.getText().toString().isEmpty() &&
                (
                        binding.sightRadioCoordinates.isChecked() &&
                        InputValidator.isValidDouble(binding.sightLongitudeInput.getText().toString()) &&
                        InputValidator.isValidDouble(binding.sightLatitudeInput.getText().toString())
                                ||
                        binding.sightRadioCurrentLocation.isChecked()
                );
    }

    // Пытаемся сохранить
    boolean save(){
        Sight sight = getSight();
        // Сохранение null чревато ошибками
        if (sight == null){
            return false;
        }
        // Сохраняем
        new Thread(() -> {
            App app = (App) getApplication();
            app.getDatabase().sightDao().insert(sight);
        }).start();
        // Успех
        return true;
    }

    Sight getSight(){
        double latitude, longitude;
        if (binding.sightRadioCurrentLocation.isChecked()){
            // Получаем latitude и longitude, исходя из текущей геолокации
            if (!coordinatesManager.areCoordsCorrect()){
                // locationManager может быть равен null,
                // поэтому уще раз вызываем инициализацию
                if (coordinatesManager.checkLocationPermission()){
                    coordinatesManager.init();
                }
                return null;
            }
            longitude = coordinatesManager.getLongitude();
            latitude = coordinatesManager.getLatitude();
        } else {
            // Получаем latitude и longitude из полей ввода
            longitude = Double.parseDouble(binding.sightLongitudeInput.getText().toString());
            latitude = Double.parseDouble(binding.sightLatitudeInput.getText().toString());
        }
        return createSight(longitude, latitude);
    }

    Sight createSight(double longitude, double latitude){
        Sight sight;
        // Проверка на создание/обновление сущности Sight
        if (id == -1){
            // Создаём новую
            sight = new Sight(
                    binding.sightTitleInput.getText().toString(),
                    binding.sightDescriptionInput.getText().toString(),
                    longitude,
                    latitude
            );
        } else {
            // Изменяем существующую
            sight = new Sight(
                    id,
                    binding.sightTitleInput.getText().toString(),
                    binding.sightDescriptionInput.getText().toString(),
                    longitude,
                    latitude
            );
        }
        return sight;
    }

    // Возвращаемся в MainActivity, в фрагмент с id = comeBackId
    void comeBack(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.GOTO_FRAGMENT, comeBackId);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        coordinatesManager.unsubscribe();
    }

    @Override
    protected void onStart() {
        super.onStart();
        coordinatesManager.subscribe();
    }
}
