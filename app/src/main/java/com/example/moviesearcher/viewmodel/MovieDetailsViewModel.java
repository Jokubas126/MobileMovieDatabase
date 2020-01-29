package com.example.moviesearcher.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.handlers.JsonHandler;
import com.example.moviesearcher.view.details.MovieDetailsFragmentArgs;

public class MovieDetailsViewModel extends ViewModel {

    private MutableLiveData<Movie> currentMovie = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Bundle arguments){
        loading.setValue(true);
        new Thread(() -> {
            if (arguments != null) {
                Movie movie = new Movie();
                movie.setId(MovieDetailsFragmentArgs.fromBundle(arguments).getMovieId());
                new JsonHandler().getMovieDetails(movie.getId(), retrievedMovie -> {
                    currentMovie.setValue((Movie) retrievedMovie);
                    loading.setValue(false);
                });
            }
        }).start();

    }

    public MutableLiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
