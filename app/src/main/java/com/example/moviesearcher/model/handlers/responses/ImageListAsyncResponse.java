package com.example.moviesearcher.model.handlers.responses;

import java.util.List;

public interface ImageListAsyncResponse {
    void processFinished(List<String> backdropPathList, List<String> posterPathList);
}
