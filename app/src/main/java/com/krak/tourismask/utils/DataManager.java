package com.krak.tourismask.utils;

import android.app.Activity;
import android.util.Log;

import com.krak.tourismask.app.App;
import com.krak.tourismask.entities.Sight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

// Класс для работы с csv файлами. Решение не идеальное, но работающее
public class DataManager {

    private static final String TAG = "DataManager";

    private static ArrayList<String> readAllLines(InputStreamReader is) throws IOException{
        ArrayList<String> strings = new ArrayList<>();
        BufferedReader reader = new BufferedReader(is);
        String mLine;
        while ((mLine = reader.readLine()) != null) {
            strings.add(mLine);
        }
        reader.close();
        return strings;
    }

    // Загружаем информацию изи sights.csv
    public static ArrayList<Sight> loadSights(){
        ArrayList<String> lines = new ArrayList<>();
        try {
            lines = readAllLines(new InputStreamReader(App.getInstance().getAssets().open("sights.csv")));
        } catch (IOException e) {
            Log.e(TAG, "Exception while reading medals.csv");
        }
        ArrayList<Sight> result = new ArrayList<>();
        int titleIndex = Arrays.asList(lines.get(0).split(";")).indexOf("title");
        int descriptionIndex = Arrays.asList(lines.get(0).split(";")).indexOf("description");
        int longitudeIndex = Arrays.asList(lines.get(0).split(";")).indexOf("longitude");
        int latitudeIndex = Arrays.asList(lines.get(0).split(";")).indexOf("latitude");
        for (int i = 1; i < lines.size(); i++) {
            String[] data = lines.get(i).split(";");
            result.add(new Sight(
                    -1,
                    data[titleIndex],
                    data[descriptionIndex],
                    Double.parseDouble(data[longitudeIndex]),
                    Double.parseDouble(data[latitudeIndex])
            ));
        }
        return result;
    }
}
