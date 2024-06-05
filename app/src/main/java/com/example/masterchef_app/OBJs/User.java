package com.example.masterchef_app.OBJs;

import java.io.Serializable;

public class  User implements Serializable {
   private String Name;
   private String email;
   private String password;
   private String userProfile ;
 private String  userKey ;
    public User() {
    }

    public User(String name, String email, String password, String userProfile, String userKey) {
        Name = name;
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
        this.userKey = userKey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
