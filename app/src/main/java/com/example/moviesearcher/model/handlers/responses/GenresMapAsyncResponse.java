package com.example.moviesearcher.model.handlers.responses;

import java.util.HashMap;

public interface GenresMapAsyncResponse {
    void processFinished(HashMap<Integer, String> genresMap);
}
