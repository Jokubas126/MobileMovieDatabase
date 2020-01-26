package com.example.moviesearcher.model.handlers;

import java.util.HashMap;

public interface GenresMapAsyncResponse {
    void processFinished(HashMap<Integer, String> genresMap);
}
