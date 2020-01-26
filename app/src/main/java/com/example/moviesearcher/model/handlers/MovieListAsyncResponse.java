package com.example.moviesearcher.model.handlers;

import com.example.moviesearcher.model.Movie;

import java.util.ArrayList;

public interface MovieListAsyncResponse {
    void processFinished(ArrayList<Movie> movieArrayList);
}
