package com.krak.tourismask.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
// Хранит позицию камеры MapFragment-а.
public class CurrentPositionViewModel extends ViewModel {
    private MutableLiveData<CameraPosition> currentPosition;

    public CurrentPositionViewModel() {
        this.currentPosition = new MutableLiveData<>();
    }

    public LiveData<CameraPosition> getData(){
        return currentPosition;
    }

    public void setValue(CameraPosition cameraPosition){
        currentPosition.setValue(cameraPosition);
    }
}
