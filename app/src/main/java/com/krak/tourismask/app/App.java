package com.krak.tourismask.app;

import android.app.Application;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import com.krak.tourismask.app.database.AppDatabase;
import com.yandex.mapkit.MapKitFactory;
/*
* Обертка над стандартным Application
* Здесь мы инициализируем MapKit, подключаем бд и убираем темную тему
*/
public class App extends Application {

    public static App instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        // Убираем тёмную тему, чтобы она ничего не сломала
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        instance = this;
        initMaps();
        initDB();
    }

    // Инициализируем базу данных в отдельном потоке
    private void initDB(){
        new CreateDatabaseTask().execute();
    }

    // Инициализируем MapKit
    private void initMaps() {
        MapKitFactory.setApiKey("70951948-173f-45f9-a418-44e2989f536e");
        MapKitFactory.initialize(this);
    }

    private class CreateDatabaseTask extends AsyncTask<Void, Void, AppDatabase>{

        @Override
        protected AppDatabase doInBackground(Void... voids) {
            AppDatabase result = Room.databaseBuilder(App.this, AppDatabase.class, Config.DB_NAME)
                    .build();
            return result;
        }

        @Override
        protected void onPostExecute(AppDatabase appDatabase) {
            super.onPostExecute(appDatabase);
            database = appDatabase;
        }
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}