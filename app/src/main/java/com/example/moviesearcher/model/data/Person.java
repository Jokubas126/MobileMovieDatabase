package com.example.moviesearcher.model.data;

import android.graphics.Bitmap;

import com.example.moviesearcher.model.util.ConverterUtil;

public class Person{

    private String name;
    private String position;
    private Bitmap profileImage;

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

    public void setProfileImage(String profileImageUrl) {
        profileImage = ConverterUtil.HttpPathToBitmap(profileImageUrl);
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
