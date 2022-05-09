package com.krak.tourismask.main_activity.fragments.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.krak.tourismask.R;
import com.krak.tourismask.databinding.MapFragmentBinding;
import com.krak.tourismask.main_activity.fragments.map.clusters.ClustersManager;
import com.krak.tourismask.main_activity.fragments.map.user_location.MapUserLocationManager;
import com.krak.tourismask.utils.CoordinatesManager;
import com.krak.tourismask.utils.CustomAnimations;
import com.krak.tourismask.utils.location_listeners.CoordinatesLocationListener;
import com.krak.tourismask.utils.location_listeners.PlacemarkLocationListener;
import com.krak.tourismask.view_models.CurrentPositionViewModel;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class MapFragment extends Fragment {

    private ClustersManager clustersManager;
    private MapUserLocationManager mapUserLocationManager;
    private MapFragmentBinding binding;
    private MapView mapView;
    private ViewModelProvider provider;
    private boolean clustersVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MapFragmentBinding.inflate(getLayoutInflater());
        mapView = binding.mapview;
        provider = new ViewModelProvider(getActivity());
        initManagers();
        addListeners();
        // Чтобы кнопка опций была видна
        binding.optionsBtn.bringToFront();
        addObservers();
        return binding.getRoot();
    }

    // Добавляем наблюдателей
    private void addObservers(){
        // Устанавливаем наблюдателя за CurrentPositionViewModel, чтобы
        // каждый раз при изменении Point, которая храниться в LiveData CurrentPositionViewModel,
        // мы приближали камеру к ней
        provider.get(CurrentPositionViewModel.class).getData().observe(getViewLifecycleOwner(), cameraPosition -> {
            if (cameraPosition != null) {
                mapView.getMap().move(
                        cameraPosition,
                        new Animation(Animation.Type.SMOOTH, 1),
            null
                );
            }
        });
    }

    // Устанавливаем слушатели нажатий
    private void addListeners() {
        // Нажата кнопка "Опции"
        binding.optionsBtn.setOnClickListener(view -> CustomAnimations.toggleVisibility(binding.options));
        // Нажата кнопка "Показать/скрыть достопримечательности"
        binding.toggleClustersBtn.setOnClickListener(view -> toggleClusters());
        // Нажата кнопка "Отдалить карту"
        binding.distanceMapBtn.setOnClickListener(view -> distanceMap());
        // Нажата кнопка "Показать/скрыть меня"
        binding.toggleMeBtn.setOnClickListener(view -> toggleMe());
    }

    // Отдаляем карту
    private void distanceMap(){
        CameraPosition oldCameraPosition = mapView.getMap().getCameraPosition();
        CameraPosition newCameraPosition = new CameraPosition(
                oldCameraPosition.getTarget(),
                Math.max(0f, oldCameraPosition.getZoom() - 2f),
                oldCameraPosition.getAzimuth(),
                oldCameraPosition.getTilt()
        );
        mapView.getMap().move(
                newCameraPosition,
                new Animation(Animation.Type.LINEAR, 1f),
                null
        );
    }

    // Показываем/скрываем placemark пользователя
    private void toggleMe(){
        mapUserLocationManager.toggleMe(binding.toggleMeBtn);
    }

    // Включаем/выключаем кластеризацию
    private void toggleClusters(){
        clustersVisible = !clustersVisible;
        if (!clustersVisible){
            clustersManager.hide();
            binding.toggleClustersBtn.setText(R.string.show_clusters);
        } else {
            clustersManager.show();
            binding.toggleClustersBtn.setText(R.string.hide_clusters);
        }
    }

    // Инициализируем вспомогательные классы
    private void initManagers() {
        clustersManager = new ClustersManager(getActivity(), mapView);
        mapUserLocationManager = new MapUserLocationManager(getActivity(), mapView.getMap().getMapObjects());
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        savePosition();
        MapKitFactory.getInstance().onStop();
    }

    // Сохраняем текущую позицию камеры в CurrentPositionViewModel
    private void savePosition() {
        provider.get(CurrentPositionViewModel.class).setValue(mapView.getMap().getCameraPosition());
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
}
