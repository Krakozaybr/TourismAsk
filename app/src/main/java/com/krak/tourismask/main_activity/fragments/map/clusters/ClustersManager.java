package com.krak.tourismask.main_activity.fragments.map.clusters;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.krak.tourismask.R;
import com.krak.tourismask.entities.Sight;
import com.krak.tourismask.view_models.SightsViewModel;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

/*
* Класс, реализующий работу с кластеризацией
*/
public class ClustersManager {

    private final ClustersListener clustersListener;
    private final FragmentActivity activity;
    private ClusterizedPlacemarkCollection collection;
    private List<PlacemarkMapObject> placemarks;
    private PlacemarkListener listener;

    public ClustersManager(FragmentActivity activity, MapView mapView) {
        this.activity = activity;
        this.listener = new PlacemarkListener(activity);
        this.clustersListener = new ClustersListener(activity);
        this.collection = mapView.getMap().getMapObjects().addClusterizedPlacemarkCollection(clustersListener);
        this.placemarks = new ArrayList<>();
    }

    public void show(){
        ImageProvider imageProvider = ImageProvider.fromResource(activity, R.drawable.cluster_icon);
        for (Sight sight : getSights()){
            // Создаем placemark
            PlacemarkMapObject placemark = collection.addPlacemark(
                    new Point(sight.getLatitude(), sight.getLongitude()),
                    imageProvider,
                    new IconStyle()
            );
            // Кладём в userData сущность Sight, она понадобится в PlacemarkListener
            placemark.setUserData(sight);
            // Добавляем placemark в список placemarks, чтобы мы могли их потом удалить
            placemarks.add(placemark);
        }
        addListeners();
        collection.clusterPlacemarks(60, 15);
    }

    // Добавляем слушатель к placemarks
    private void addListeners(){
        for (PlacemarkMapObject placemark : placemarks){
            placemark.addTapListener(listener);
        }
    }

    // Скрываем кластеризацию
    public void hide(){
        for (PlacemarkMapObject placemark : placemarks){
            collection.remove(placemark);
        }
        collection.clusterPlacemarks(60, 15);
    }

    // Получаем Sight объекты из SightsViewModel
    private List<Sight> getSights() {
        ViewModelProvider provider = new ViewModelProvider(activity);
        SightsViewModel viewModel = provider.get(SightsViewModel.class);
        return viewModel.getData().getValue();
    }
}
