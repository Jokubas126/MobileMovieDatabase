package com.example.moviesearcher.model.data;

import android.graphics.Bitmap;

public class Person{

    private Bitmap profileImage;
    private String name;
    private String position;

    public Bitmap getProfileImage() { return profileImage; }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
