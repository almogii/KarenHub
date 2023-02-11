package com.example.karenhub.model;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Post {
    @PrimaryKey
    @NonNull
    public String id="";
    public String title ="";
    public String imgUrl ="";
    public String details = "";
    public String location="";
    public String label="";
    public Long timestamp;
    @Ignore
    public Post(){
    }
    public Post(String id, String title, String imgUrl, String details,String location, String label,Long timestamp) {
        this.title = title;
        this.id = id;
        this.imgUrl = imgUrl;
        this.details = details;
        this.location=location;
        this.label=label;
        this.timestamp=timestamp;

    }

    static final String TITLE = "title";
    static final String LABEL = "label";
    static final String ID = "id";
    static final String IMAGE = "image";
    static final String DETAILS = "details";
    static final String LOCATION = "location";
    static final String TIMESTAMP = "timestamp";
    static final String COLLECTION = "posts";


    public static Post fromJson(Map<String, Object> json) {
        String id = (String) json.get(ID);
        String label = (String) json.get(LABEL);
        String name = (String) json.get(TITLE);
        String image = (String) json.get(IMAGE);
        String details = (String) json.get(DETAILS);
        String location = (String) json.get(LOCATION);
        Long timestamp =(Long) json.get(TIMESTAMP);

        Post post = new Post(id, name, image, details, location, label, timestamp);
        return post;
    }

    public  Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(TITLE, getTitle());
        json.put(IMAGE, getImgUrl());
        json.put(DETAILS, getDetails());
        json.put(LOCATION,getLocation());
        json.put(LABEL,getLabel());
        json.put(TIMESTAMP,getTimestamp());


        return json;
    }


    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public void setId(@NonNull String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    @NonNull
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public String getDetails() {
        return details;
    }
    public String getLocation() {
        return location;
    }
    public String getLabel() {
        return label;
    }
    public Long getTimestamp() {return this.timestamp;}


}
