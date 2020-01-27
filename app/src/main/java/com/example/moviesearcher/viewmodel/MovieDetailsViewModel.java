package com.example.moviesearcher.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.Movie;
import com.example.moviesearcher.model.handlers.JsonHandler;
import com.example.moviesearcher.view.MovieDetailsFragmentArgs;

public class MovieDetailsViewModel extends ViewModel {

    private MutableLiveData<Movie> currentMovie = new MutableLiveData<Movie>();


    public void fetch(Bundle arguments){
        Movie movie = new Movie();

        new Thread(() -> {
            if (arguments != null){
                movie.setId(MovieDetailsFragmentArgs.fromBundle(arguments).getMovieId());
                new JsonHandler().getMovieDetails(movie.getId(), retrievedMovie -> {
                    currentMovie.setValue(retrievedMovie);
                });
            }
        }).start();
    }

    public MutableLiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }
}
