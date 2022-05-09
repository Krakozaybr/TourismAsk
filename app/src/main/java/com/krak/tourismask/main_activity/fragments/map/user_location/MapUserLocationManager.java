package com.krak.tourismask.main_activity.fragments.map.user_location;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.widget.AppCompatButton;

import com.krak.tourismask.R;
import com.krak.tourismask.utils.CoordinatesManager;
import com.krak.tourismask.utils.location_listeners.CoordinatesLocationListener;
import com.krak.tourismask.utils.location_listeners.PlacemarkLocationListener;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.runtime.image.ImageProvider;

// Класс отвечает за отображение текущего местоположения пользователя на карте
public class MapUserLocationManager {

    private CoordinatesManager coordinatesManager;
    private MapObjectCollection collection;
    private PlacemarkMapObject userPlacemark = null;
    private boolean isUserVisible = false;
    private ImageProvider imageProvider;

    public MapUserLocationManager(Activity activity, MapObjectCollection collection) {
        this.collection = collection;
        this.coordinatesManager = new CoordinatesManager(activity, new CoordinatesLocationListener());
        this.imageProvider = ImageProvider.fromBitmap(
                BitmapFactory.decodeResource(activity.getResources(), R.drawable.me));
    }

    public void toggleMe(AppCompatButton toggleMeBtn){
        if (!isUserVisible){
            // Пытаемся получить доступ к геолокации
            coordinatesManager.connect();
            if (coordinatesManager.checkLocationPermission()){
                // Доступ получен, значит можно переключить флаг isUserVisible
                isUserVisible = true;
                // Текст кнопки тоже меняем только при получении доступа
                toggleMeBtn.setText(R.string.hide_me);
                // Добавляем placemark с изображением пользователя
                userPlacemark = collection.addPlacemark(coordinatesManager.getPosition(), imageProvider);
                // Станет видимым при получении достоверных координат, а не (-1, -1)
                userPlacemark.setVisible(false);
                coordinatesManager.setLocationListener(new PlacemarkLocationListener(userPlacemark));
                coordinatesManager.subscribe();
            }
        } else {
            isUserVisible = false;
            toggleMeBtn.setText(R.string.show_me);
            coordinatesManager.unsubscribe();
            if (userPlacemark != null){
                // Удаляем placemark с изображением пользователя
                collection.remove(userPlacemark);
            }
        }
    }
}
