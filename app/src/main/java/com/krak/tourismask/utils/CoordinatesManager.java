package com.krak.tourismask.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.krak.tourismask.utils.location_listeners.CustomLocationListener;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
/*
* Класс для работы с геолокацией MapKit-а
*/
public class CoordinatesManager {

    private LocationManager locationManager = null;
    private Activity activity;
    private CustomLocationListener locationListener = null;

    public CoordinatesManager(Activity activity, CustomLocationListener locationListener) {
        this.activity = activity;
        this.locationListener = locationListener;
    }

    public void init(){
        Log.e("init", "init");
        if (locationManager == null) {
            this.locationManager = MapKitFactory.getInstance().createLocationManager();
        }
        subscribe();
    }

    // Подписываем слушателя на locationManager-а
    public void subscribe(){
        Log.e("subscribe", "subscribe");
        if (locationManager != null) {
            this.locationManager.subscribeForLocationUpdates(
                    0, 90, 0, false, FilteringMode.OFF, locationListener
            );
        }
    }

    // Убираем подписку слушателя на locationManager-а
    public void unsubscribe(){
        Log.e("unsubscribe", "unsubscribe");
        if (locationManager != null) {
            locationManager.unsubscribe(locationListener);
        }
    }

    // Устанавливаем новый locationListener
    public void setLocationListener(CustomLocationListener locationListener){
        // Но прежде убираем из подписчиков предыдущий
        unsubscribe();
        this.locationListener = locationListener;
    }

    // Подключаемся к геолокации
    public void connect(){
        // Пытаемся получить разрешение пользователя на местоположение
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123
        );
        // При успехе инициализируем locationManager
        if (checkLocationPermission()){
            init();
        }
    }

    // Проверка наличия разрешения на геолокацию
    public boolean checkLocationPermission(){
        return ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public Point getPosition(){
        return new Point(getLatitude(), getLongitude());
    }

    public double getLatitude() {
        return locationListener.getLatitude();
    }

    public double getLongitude() {
        return locationListener.getLongitude();
    }

    // Проверка координат на корректность
    public boolean areCoordsCorrect(){
        return locationListener.getLongitude() != -1 && locationListener.getLatitude() != -1;
    }
}
