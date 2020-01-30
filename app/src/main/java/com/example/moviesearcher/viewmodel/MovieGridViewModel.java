package com.example.moviesearcher.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.handlers.JsonHandler;

import java.util.List;

public class MovieGridViewModel extends AndroidViewModel {

    private Activity activity;

    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private int page;

    public MovieGridViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        movieLoadError.setValue(false);
        loading.setValue(true);
        page = 1;
        new Thread(() -> new JsonHandler().getMovieList(page, list -> {
            if (list.isEmpty()){
                activity.runOnUiThread(() -> {
                    movies.setValue(null);
                    movieLoadError.setValue(true);
                    loading.setValue(false);
                });
            } else {
                activity.runOnUiThread(() -> {
                    movies.setValue(list);
                    movieLoadError.setValue(false);
                    loading.setValue(false);
                });
            }
        })).start();
    }

    public void fetch(){
        movieLoadError.setValue(false);
        loading.setValue(true);
        page++;
        new Thread(() -> new JsonHandler().getMovieList(page, list -> {
            if (list.isEmpty()){
                activity.runOnUiThread(() -> {
                    movies.setValue(null);
                    movieLoadError.setValue(true);
                    loading.setValue(false);
                });
            } else {
                activity.runOnUiThread(() -> {
                    movies.setValue(list);
                    movieLoadError.setValue(false);
                    loading.setValue(false);
                });
            }
        })).start();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public MutableLiveData<List<Movie>> getMovies() {
        return movies;
    }

    public MutableLiveData<Boolean> getMovieLoadError() {
        return movieLoadError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
