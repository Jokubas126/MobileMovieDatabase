package com.example.moviesearcher.model.data;

import android.graphics.Bitmap;

import com.example.moviesearcher.model.util.ConverterUtil;

import java.util.List;

public class Movie{

    private int id = 0;
    private Bitmap posterImage;
    private Bitmap backdropImage;
    private String title;
    private String releaseDate;
    private String score;
    private String genres;
    private String productionCountries;
    private int runtime;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getPosterImage() {
        return posterImage;
    }

    public Bitmap getBackdropImage() {
        return backdropImage;
    }

    public void setPosterImage(String posterImageUrl) {
        posterImage = ConverterUtil.HttpPathToBitmap(posterImageUrl);
    }

    public void setBackdropImage(String backdropImageUrl) {
        backdropImage = ConverterUtil.HttpPathToBitmap(backdropImageUrl);
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

    public void setGenres(List<String> genresList) {
        new Thread(() -> genres = ConverterUtil.stringListToString(genresList)).start();
    }

    public void setGenres(String genres){
        this.genres = genres;
    }

    public String getGenres() {
        return genres;
    }

    public String getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<String> productionCountriesList) {
        new Thread(() -> productionCountries = ConverterUtil.stringListToString(productionCountriesList)).start();
    }

    public void setProductionCountries(String productionCountries) {
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
}
