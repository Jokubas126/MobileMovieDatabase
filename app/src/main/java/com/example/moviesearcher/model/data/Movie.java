package com.example.moviesearcher.model.data;

import java.util.List;

import static com.example.moviesearcher.util.ConverterUtilKt.stringListToString;

public class Movie{

    private int id = 0;
    private String title;
    private String releaseDate;
    private String score;
    private String genres;
    private String productionCountries;
    private int runtime;
    private String description;
    private String posterImageUrl;
    private String backdropImageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setBackdropImageUrl(String backdropImageUrl) {
        this.backdropImageUrl = backdropImageUrl;
    }

    public String getBackdropImageUrl() {
        return backdropImageUrl;
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
        new Thread(() -> genres = stringListToString(genresList)).start();
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
        new Thread(() -> productionCountries = stringListToString(productionCountriesList)).start();
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
