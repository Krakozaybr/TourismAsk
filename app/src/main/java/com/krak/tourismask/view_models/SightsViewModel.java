package com.krak.tourismask.view_models;

import android.app.Application;

import com.krak.tourismask.app.App;
import com.krak.tourismask.app.database.AppDatabase;
import com.krak.tourismask.app.database.SightDao;
import com.krak.tourismask.entities.Sight;
import com.krak.tourismask.utils.DataManager;

import java.util.ArrayList;
import java.util.List;
/*
* Класс для взаимодействия с бд, хранения сущностей Sight
* Все действия происходят в отдельных потоках
*/
public class SightsViewModel extends ListViewModel<Sight> {

    public SightsViewModel(Application application) {
        super(application);
    }

    // Загружаем сущности Sight
    @Override
    public void loadData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                // Загружаем из бд
                App app = getApplication();
                AppDatabase db = app.getDatabase();
                SightDao dao = db.sightDao();
                List<Sight> result = dao.getAll();
                // Загружаем из sights.csv
                result.addAll(DataManager.loadSights());
                // Обновляем
                data.postValue(result);
            }
        }.start();
    }

    // Загружаем данные в бд
    @Override
    public void uploadData() {
        new Thread(() -> {
            App app = getApplication();
            AppDatabase db = app.getDatabase();
            SightDao sightDao = db.sightDao();
            if (getData() != null && getData().getValue() != null) {
                List<Sight> sightList = new ArrayList<>();
                for (Sight sight : getData().getValue()){
                    // Проверка на встроенные Sight. Их в бд класть не нужно
                    if (sight.getId() != -1){
                        sightList.add(sight);
                    }
                }
                sightDao.insertAll(sightList);
            }
        }).start();
    }

    // Удаляем сущность Sight из бд и динамического хранилища SightsViewModel
    public void delete(Sight sight){
        // Удаляем из динамического хранилища SightsViewModel
        List<Sight> updated = getData().getValue();
        updated.remove(sight);
        updateData(updated);
        new Thread(() -> {
            // Удаляем из бд в отдельном потоке
            App app = getApplication();
            AppDatabase db = app.getDatabase();
            SightDao sightDao = db.sightDao();
            sightDao.delete(sight);
        }).start();
    }
}
