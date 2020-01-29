package com.example.moviesearcher.model.util;

public class UrlUtil {

    private UrlUtil(){}

    public static final String KEY_POPULAR = "popular";

    private static final String API_KEY = "c6c88c6e91d206e35fa6aff0b9d1cc36";
    private static final String BASE_URL = "https://api.themoviedb.org";

    private static final String BASE_PROFILE_IMAGE_URL = "https://image.tmdb.org/t/p/w300";
    private static final String BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w400";
    private static final String BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w780";

    private static final String BASE_GENRES_URL = "https://api.themoviedb.org/3/genre/movie";
    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_YOUTUBE_VIDEO = "https://www.youtube.com/watch?v=";

    public static String getMovieListUrl(String key, int page){
        return BASE_MOVIE_URL + key + "?api_key=" + API_KEY + "&language=en-US&page=" + page;
    }

    public static String getMovieGenresUrl(){
        return BASE_GENRES_URL + "/list?api_key=" + API_KEY + "&language=en-US";
    }

    public static String getMovieDetailsUrl(int id){ return BASE_MOVIE_URL + id + "?api_key=" + API_KEY + "&language=en-US"; }
    public static String getPeopleUrl(int id){ return BASE_MOVIE_URL + id + "/credits?api_key=" + API_KEY; }

    public static String getPosterImageUrl(String imagePath){ return BASE_POSTER_IMAGE_URL + imagePath; }
    public static String getBackdropImageUrl(String imagePath){ return BASE_BACKDROP_IMAGE_URL + imagePath; }
    public static String getProfileImageUrl(String imagePath){ return BASE_PROFILE_IMAGE_URL + imagePath; }

    public static String getTrailerVideo(String videoKey){
        return BASE_YOUTUBE_VIDEO + videoKey;
    }
}