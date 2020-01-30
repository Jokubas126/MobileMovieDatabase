package com.example.moviesearcher.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.handlers.JsonHandler;

public class MovieOverviewViewModel extends ViewModel {

    private MutableLiveData<Movie> currentMovie = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Bundle arguments){
        loading.setValue(true);
        new Thread(() -> {
            if (arguments != null) {
                new JsonHandler().getMovieDetails(arguments.getInt("movieId"), retrievedMovie -> {
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
