package com.example.moviesearcher.model.handlers;

import com.example.moviesearcher.model.Movie;

public interface MovieDetailsAsyncResponse {
    void processFinished(Movie movie);
}
