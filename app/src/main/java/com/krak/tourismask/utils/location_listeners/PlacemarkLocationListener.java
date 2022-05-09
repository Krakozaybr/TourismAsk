package com.krak.tourismask.utils.location_listeners;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.map.PlacemarkMapObject;
/*
* Помимо функциональности CoordinatesLocationListener,
* при обновлении данных о геолокации обновляет положение placemark.
*/
public class PlacemarkLocationListener extends CoordinatesLocationListener{

    private PlacemarkMapObject placemark;

    public PlacemarkLocationListener(PlacemarkMapObject placemark) {
        this.placemark = placemark;
    }

    @Override
    public void onLocationUpdated(Location location) {
        super.onLocationUpdated(location);
        // Проверяем на валидность координаты и placemark
        if (placemark.isValid() && getLatitude() != -1 && getLatitude() != -1) {
            // Меняем местоположение placemark
            placemark.setGeometry(new Point(getLatitude(), getLongitude()));

            // Изначально placemark невидима. При первом получении валидных данных
            // её видимость устанавливается в Visible
            if (!placemark.isVisible()){
                placemark.setVisible(true);
            }
        }
    }
}
