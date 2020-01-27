package com.example.moviesearcher.model.util;

public class JsonUtil {

    private static JsonUtil instance;

    private JsonUtil(){}

    public static JsonUtil getInstance() {
        if (instance == null)
            instance = new JsonUtil();
        return instance;
    }

    private static final String API_KEY = "c6c88c6e91d206e35fa6aff0b9d1cc36";
    private static final String BASE_URL = "https://api.themoviedb.org";

    private static final String BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w300";
    private static final String BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    // ending can go from 1 to 1000
    public static final String POPULAR_MOVIE_LIST_URL = "https://api.themoviedb.org/3/movie/popular?api_key=c6c88c6e91d206e35fa6aff0b9d1cc36&page=1";
    public static final String MOVIE_GENRES_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=c6c88c6e91d206e35fa6aff0b9d1cc36&language=en-US";

    private static final String BASE_MOVIE_DETAILS = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_YOUTUBE_VIDEO = "https://www.youtube.com/watch?v=";

    public String getMovieDetailsURL(int id){ return BASE_MOVIE_DETAILS + id + "?api_key=" + API_KEY + "&language=en-US"; }

    public String getPosterImageUrl(String imagePath){
        return BASE_POSTER_IMAGE_URL + imagePath;
    }
    public String getBackdropImageUrl(String imagePath){ return BASE_BACKDROP_IMAGE_URL + imagePath; }

    public String getTrailerVideo(String videoKey){
        return BASE_YOUTUBE_VIDEO + videoKey;
    }
}
