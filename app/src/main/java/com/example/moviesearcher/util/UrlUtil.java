package com.example.moviesearcher.util;

public class UrlUtil {

    private UrlUtil() {
    }

    private static final String API_KEY = "c6c88c6e91d206e35fa6aff0b9d1cc36";
    private static final String BASE_URL = "https://api.themoviedb.org";
    private static final String LANGUAGE_KEY = "&language=en-US";

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w780";

    private static final String BASE_GENRES_URL = "https://api.themoviedb.org/3/genre/movie";
    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String BASE_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key=";
    private static final String BASE_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=c6c88c6e91d206e35fa6aff0b9d1cc36&sort_by=popularity.desc&page=";

    private static final String BASE_CONFIGURATION_LANGUAGES = "https://api.themoviedb.org/3/configuration/languages?api_key=";

    public static String getMovieListUrl(String key, int page) {
        return BASE_MOVIE_URL + key + "?api_key=" + API_KEY + LANGUAGE_KEY + "&page=" + page;
    }

    public static String getMovieGenresUrl() {
        return BASE_GENRES_URL + "/list?api_key=" + API_KEY + LANGUAGE_KEY;
    }

    public static String getMovieDetailsUrl(int id) {
        return BASE_MOVIE_URL + id + "?api_key=" + API_KEY + LANGUAGE_KEY;
    }

    public static String getPeopleUrl(int id) {
        return BASE_MOVIE_URL + id + "/credits?api_key=" + API_KEY;
    }

    public static String getImageUrl(String imagePath) {
        return BASE_IMAGE_URL + imagePath;
    }

    public static String getMovieVideosUrl(int id) {
        return BASE_MOVIE_URL + id + "/videos?api_key=" + API_KEY + LANGUAGE_KEY;
    }

    public static String getMovieImagesUrl(int id) {
        return BASE_MOVIE_URL + id + "/images?api_key=" + API_KEY + "&language=en";
    }

    public static String getLanguagesUrl() {
        return BASE_CONFIGURATION_LANGUAGES + API_KEY;
    }

    public static String getSearchUrl(String searchKey, int page) {
        return BASE_SEARCH_URL + API_KEY + LANGUAGE_KEY + "&query=" + searchKey + "&page=" + page;
    }

    public static String getDiscoverUrl(int genreId, String languageKey, String startYear, String endYear, int page) {
        String url = BASE_DISCOVER_URL + page;
        if (!startYear.equals("∞"))
            url = url + "&primary_release_date.gte=" + startYear + "-01-01";
        url = url + "&primary_release_date.lte=" + endYear + "-12-31";
        if (genreId != 0)
            url = url + "&with_genres=" + genreId;
        if (languageKey != null)
            url = url + "&with_original_language=" + languageKey;
        return url;
    }
}
