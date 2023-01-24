package com.example.karenhub.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public Post(){
    }
    public Post(String id, String title, String imgUrl, String details) {
        this.title = title;
        this.id = id;
        this.imgUrl = imgUrl;
        this.details = details;
    }

    static final String TITLE = "title";
    static final String ID = "id";
    static final String IMAGE = "image";
    static final String DETAILS = "details";
    static final String COLLECTION = "posts";

    public static Post fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String name = (String)json.get(TITLE);
        String image = (String)json.get(IMAGE);
        String details = (String) json.get(DETAILS);
        Post post = new Post(id,name,image,details);
        return post;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(TITLE, getTitle());
        json.put(IMAGE, getImgUrl());
        json.put(DETAILS, getDetails());
        return json;
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
}