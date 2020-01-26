package com.example.moviesearcher.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsViewModel extends ViewModel {

    private MutableLiveData<Movie> movie = new MutableLiveData<Movie>();


    public void fetch(){
        Movie movie1 = new Movie();
        movie1.setId(0);
        movie1.setTitle("Avengers: Endgame");
        movie1.setReleaseDate("2019");
        movie1.setLanguage("USA");
        movie1.setRuntime(135);
        List<String> genres = new ArrayList<>();
        genres.add("Action");
        genres.add("Sci-Fi");
        genres.add("Comedy");
        genres.add("Superhero");
        movie1.setGenres(genres);
        movie1.setIMDbScore("8.8");
        movie1.setDescription("TMDb offers a powerful API service that is free to use as long as you properly attribute us as the source of the data and/or images you use. You can find the logos for attribution here.");
        movie.setValue(movie1);
    }

    public MutableLiveData<Movie> getMovie() {
        return movie;
    }
}
