package com.example.moviesearcher.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.moviesearcher.model.Movie;
import com.example.moviesearcher.model.handlers.JsonHandler;

import java.util.ArrayList;
import java.util.List;

public class MovieGridViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> movies = new MutableLiveData<List<Movie>>();
    private MutableLiveData<Boolean> movieLoadError = new MutableLiveData<Boolean>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    public MovieGridViewModel(@NonNull Application application) {
        super(application);
    }

    public void refresh(){

        ArrayList<Movie> movieList = new ArrayList<>();
        movies.setValue(movieList);
        movieLoadError.setValue(false);
        loading.setValue(true);

        JsonHandler handler = new JsonHandler();
        handler.getMoviesFromJson(movieArrayList -> {
            Log.d("HandlerRequest", "onCreate: REQUEST FROM THE HANDLER MADE");
            movieList.addAll(handler.getMovieList());
            movies.setValue(movieList);
            movieLoadError.setValue(false);
            loading.setValue(false);
        });
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
