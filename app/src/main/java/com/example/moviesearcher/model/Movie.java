package com.example.moviesearcher.model;

public class Movie {
    private int id = 0;
    private String title;

    public Movie(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
