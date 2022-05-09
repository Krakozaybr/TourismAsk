package com.krak.tourismask.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

/*
* Обёртка над SharedPreferences
*/
public class PreferenceManager {

    private static final String PREFERENCES = "PREFERENCES";
    private static final String NAME = "NAME";
    private static final String ADVICES = "ADVICES";

    private SharedPreferences preferences;

    public PreferenceManager(AppCompatActivity context) {
        this.preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    private void putString(String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getString(String key, String defaultValue){
        return preferences.getString(key, defaultValue);
    }

    public void saveName(String name){
        putString(NAME, name);
    }

    public String getName(){
        return getString(NAME, "");
    }

    private void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue){
        return preferences.getBoolean(key, defaultValue);
    }

    public void saveAdvices(boolean visible){
        putBoolean(ADVICES, visible);
    }

    public boolean getAdvices(){
        return getBoolean(ADVICES, true);
    }
}
