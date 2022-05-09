package com.krak.tourismask.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/*
 * Обеспечивает взаимодействие Activity с AppDatabase
 * Модель.
 */
public abstract class ListViewModel<V> extends AndroidViewModel {

    MutableLiveData<List<V>> data = new MutableLiveData<>();

    public ListViewModel(Application application) {
        super(application);
    }

    public LiveData<List<V>> getData(){
        return data;
    }

    public synchronized void updateData(List<V> newData){
        data.setValue(newData);
    }

    public abstract void loadData();

    public abstract void uploadData();
}
