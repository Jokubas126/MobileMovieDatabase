package com.example.moviesearcher.ui.details.overview;

import android.app.Activity;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.services.MovieDbApiService;
import com.example.moviesearcher.util.BundleUtil;

public class OverviewViewModel extends ViewModel {

    private MutableLiveData<Movie> currentMovie = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public void fetch(Activity activity, Bundle arguments){
        loading.setValue(true);
        new Thread(() -> {
            if (arguments != null) {
                new MovieDbApiService().getMovieDetails(arguments.getInt(BundleUtil.KEY_MOVIE_ID),
                        retrievedMovie -> activity.runOnUiThread(() -> {
                    currentMovie.setValue((Movie) retrievedMovie);
                    loading.setValue(false);
                }));
            }
        }).start();
    }

    LiveData<Movie> getCurrentMovie() {
        return currentMovie;
    }

    LiveData<Boolean> getLoading() {
        return loading;
    }
}
