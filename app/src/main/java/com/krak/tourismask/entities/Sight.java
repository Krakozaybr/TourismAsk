package com.krak.tourismask.entities;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

/*
* Сущность Sight. Может храниться в бд. Уникальный ключ - id.
*/
@Entity(tableName = "sights")
public class Sight {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title, description;
    private double longitude, latitude;

    public Sight(long id, String title, String description, double longitude, double latitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Sight(String title, String description, double longitude, double latitude) {
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Sight() {
    }

    public String toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("title", title);
            jsonObject.put("description", description);
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);
        } catch (JSONException e){
            Log.e("Sight", "JSONException while converting Sight obj to json");
        }
        return jsonObject.toString();
    }

    public static Sight parse(String json){
        Sight sight = new Sight();
        try {
            JSONObject jsonObject = new JSONObject(json);
            sight.setId(jsonObject.getInt("id"));
            sight.setTitle(jsonObject.getString("title"));
            sight.setDescription(jsonObject.getString("description"));
            sight.setLongitude(jsonObject.getDouble("longitude"));
            sight.setLatitude(jsonObject.getDouble("latitude"));
        } catch (JSONException e){
            Log.e("Sight", "JSONException while parsing json of Sight obj");
        }
        return sight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
