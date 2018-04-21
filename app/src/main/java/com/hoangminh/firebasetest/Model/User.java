package com.hoangminh.firebasetest.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class User {
    private String userId;
    private String userName;
    private String userCity;

    public User(){

    }

    public User(String userId, String userName, String userCity) {
        this.userId = userId;
        this.userName = userName;
        this.userCity = userCity;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserCity() {
        return userCity;
    }
}