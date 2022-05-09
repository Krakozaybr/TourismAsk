package com.krak.tourismask.utils.location_listeners;

import android.util.Log;

import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationStatus;
/*
* Собственно, стандартная реализация CustomLocationListener.
* Хранит у себя координаты, при необходимости предоставляет их.
*/
public class CoordinatesLocationListener implements CustomLocationListener {

    private double longitude = -1, latitude = -1;

    @Override
    public void onLocationUpdated(Location location) {
        latitude = location.getPosition().getLatitude();
        longitude = location.getPosition().getLongitude();
        Log.e("UPD", "UPD");
    }

    @Override
    public void onLocationStatusUpdated(LocationStatus locationStatus) {

    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }
}
