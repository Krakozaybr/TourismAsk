package com.krak.tourismask.utils.location_listeners;

import com.yandex.mapkit.location.LocationListener;
/*
* Хранится в CoordinatesManager
* Помимо методов LocationListener имеет методы получения координат,
* т.к. предполагается, что его реализации будут хранить данные у себя
* и предоставлять их при необходимости.
*/
public interface CustomLocationListener extends LocationListener {
    double getLatitude();
    double getLongitude();
}
