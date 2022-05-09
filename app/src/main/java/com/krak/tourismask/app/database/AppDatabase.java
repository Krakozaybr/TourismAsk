package com.krak.tourismask.app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.krak.tourismask.entities.Sight;
/*
* Для взаимодействия с бд я использую Room
*/
@Database(entities = {Sight.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SightDao sightDao();
}
