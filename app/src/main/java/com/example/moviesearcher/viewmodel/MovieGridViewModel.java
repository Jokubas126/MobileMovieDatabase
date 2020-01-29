package com.example.moviesearcher.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesearcher.model.data.Movie;
import com.example.moviesearcher.model.handlers.JsonHandler;

import java.util.List;

public class MovieGridViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();
    private MutableLiveData<Boolean> movieLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public MovieGridViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){
        movieLoadError.setValue(false);
        loading.setValue(true);
        new Thread(() -> new JsonHandler().getMovieList(list -> {
            if (list.isEmpty()){
                movies.setValue(null);
                movieLoadError.setValue(true);
                loading.setValue(false);
            } else {
                movies.setValue(list);
                movieLoadError.setValue(false);
                loading.setValue(false);
            }
        })).start();

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
