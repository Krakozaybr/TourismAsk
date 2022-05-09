package com.krak.tourismask.main_activity.fragments.map.clusters;

import androidx.fragment.app.FragmentActivity;

import com.krak.tourismask.dialogs.MessageDialog;
import com.krak.tourismask.entities.Sight;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;

public class PlacemarkListener implements MapObjectTapListener {

    private FragmentActivity activity;

    public PlacemarkListener(FragmentActivity activity) {
        this.activity = activity;
    }

    // Показываем диалоговое окно с информацией о достопримечательности,
    // которую берем из Sight, сохраненного в userData mapObject-а
    @Override
    public boolean onMapObjectTap(MapObject mapObject, Point point) {
        Sight sight = (Sight) mapObject.getUserData();
        new MessageDialog(sight.getTitle(), sight.getDescription())
                .show(activity.getSupportFragmentManager(), "custom");
        return true;
    }
}
