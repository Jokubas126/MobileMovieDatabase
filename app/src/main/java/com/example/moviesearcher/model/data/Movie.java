package com.example.moviesearcher.model.data;

import android.graphics.Bitmap;

import java.util.List;

public class Movie{

    private int id = 0;
    private Bitmap posterImage;
    private Bitmap backdropImage;
    private String title;
    private String releaseDate;
    private String score;
    private List<String> genres;
    private List<String> productionCountries;
    private int runtime;
    private String description;
    private boolean isAdult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(Bitmap posterImage) {
        this.posterImage = posterImage;
    }

    public Bitmap getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(Bitmap backdropImage) {
        this.backdropImage = backdropImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<String> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }
}
