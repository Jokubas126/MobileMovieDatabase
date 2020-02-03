package com.example.moviesearcher.util;

public class UrlUtil {

    private UrlUtil(){}

    private static final String API_KEY = "c6c88c6e91d206e35fa6aff0b9d1cc36";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final String LANGUAGE_KEY = "&language=en-US";

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w780";

    private static final String BASE_GENRES_URL = "https://api.themoviedb.org/3/genre/movie";
    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";

    public static String getMovieListUrl(String key, int page){
        return BASE_MOVIE_URL + key + "?api_key=" + API_KEY + LANGUAGE_KEY + "&page=" + page;
    }

    public static String getMovieGenresUrl(){
        return BASE_GENRES_URL + "/list?api_key=" + API_KEY + LANGUAGE_KEY;
    }

    public static String getMovieDetailsUrl(int id){ return BASE_MOVIE_URL + id + "?api_key=" + API_KEY + LANGUAGE_KEY; }
    public static String getPeopleUrl(int id){ return BASE_MOVIE_URL + id + "/credits?api_key=" + API_KEY; }

    public static String getImageUrl(String imagePath) { return BASE_IMAGE_URL + imagePath; }

    public static String getMovieVideosUrl(int id){ return BASE_MOVIE_URL + id + "/videos?api_key=" + API_KEY + LANGUAGE_KEY; }
    public static String getMovieImagesUrl(int id) {return  BASE_MOVIE_URL + id + "/images?api_key=" + API_KEY + "&language=en"; }
}
