package com.krak.tourismask.main_activity.fragments.map.clusters;

import android.app.Activity;
import android.widget.Toast;

import com.yandex.mapkit.map.Cluster;
import com.yandex.mapkit.map.ClusterListener;
import com.yandex.mapkit.map.ClusterTapListener;
/*
* Реализация слушателя событий с кластерами:
*  - добавление кластера
*  - нажатие на кластер
*/
public class ClustersListener implements ClusterListener, ClusterTapListener {

    private final Activity activity;

    public ClustersListener(Activity activity) {
        this.activity = activity;
    }

    // Добавление кластера
    @Override
    public void onClusterAdded(Cluster cluster) {
        cluster.getAppearance().setIcon(
                new TextImageProvider(Integer.toString(cluster.getSize()), activity));
        cluster.addClusterTapListener(this);
    }

    // Нажатие на кластер
    @Override
    public boolean onClusterTap(Cluster cluster) {
        Toast.makeText(activity,
                "Вы нажали на кластер с " + cluster.getSize() + " элементами",
                Toast.LENGTH_SHORT).show();
        return true;
    }
}
