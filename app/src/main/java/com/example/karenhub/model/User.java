package com.example.karenhub.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String email="";
    public String label ="";

    static final String EMAIL = "email";
    static final String ACCOUNT_LABEL = "label";
    static final String COLLECTION = "users";

    User() {

    }

    public User(String email, String label) {
        setEmail(email);
        setLabel(label);
    }

    public static User fromJson(Map<String,Object> json){
        String email = (String)json.get(EMAIL);
        String label = (String)json.get(ACCOUNT_LABEL);
        User user = new User(email,label);
        return user;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(EMAIL, getEmail());
        json.put(ACCOUNT_LABEL, getLabel());
        return json;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
