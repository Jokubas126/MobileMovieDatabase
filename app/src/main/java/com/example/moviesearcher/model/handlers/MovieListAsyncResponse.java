package com.example.moviesearcher.model.handlers;

import com.example.moviesearcher.model.data.Movie;

import java.util.List;

public interface MovieListAsyncResponse {
    void processFinished(List<Movie> movieList);
}
