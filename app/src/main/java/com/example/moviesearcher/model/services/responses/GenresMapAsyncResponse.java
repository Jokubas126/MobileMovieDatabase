package com.example.moviesearcher.model.services.responses;

import java.util.HashMap;

public interface GenresMapAsyncResponse {
    void processFinished(HashMap<Integer, String> genresMap);
}
