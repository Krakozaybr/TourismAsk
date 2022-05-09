package com.krak.tourismask.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.krak.tourismask.entities.Sight;

import java.util.ArrayList;
import java.util.List;

/*
* DataAccessObject для получения и CrUD объектов Sight
*
* Примечание:
* @Insert(onConflict = OnConflictStrategy.REPLACE)
* Означает, что при наличии объекта с тем же id, он будет заменен.
* В ином случае просто добавится еще одна строка в таблицу.
* Таким образом через один метод реализована вставка и создание строки.
*/
@Dao
public interface SightDao {
    // Вставка списка объекта
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Sight> sights);

    // Вставка одного объекта
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sight sight);

    // Удаление одного объекта
    @Delete
    void delete(Sight sight);

    // Получение всех объектов из бд
    @Query("SELECT * FROM sights ORDER BY id")
    List<Sight> getAll();
}
