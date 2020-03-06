package com.example.moviesearcher.util

private const val API_KEY = "c6c88c6e91d206e35fa6aff0b9d1cc36"
private const val BASE_URL = "https://api.themoviedb.org"
private const val LANGUAGE_KEY = "&language=en-US"

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w780"

private const val BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/"

private const val BASE_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key="
private const val BASE_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?api_key=c6c88c6e91d206e35fa6aff0b9d1cc36&sort_by=popularity.desc&page="

private const val BASE_CONFIGURATION_LANGUAGES = "https://api.themoviedb.org/3/configuration/languages?api_key="

fun getPeopleUrl(id: Int): String? {
    return "$BASE_MOVIE_URL$id/credits?api_key=$API_KEY"
}

fun getImageUrl(imagePath: String): String? {
    return BASE_IMAGE_URL + imagePath
}

fun getMovieVideosUrl(id: Int): String? {
    return "$BASE_MOVIE_URL$id/videos?api_key=$API_KEY$LANGUAGE_KEY"
}

fun getLanguagesUrl(): String? {
    return BASE_CONFIGURATION_LANGUAGES + API_KEY
}

fun getSearchUrl(searchKey: String, page: Int): String? {
    return "$BASE_SEARCH_URL$API_KEY$LANGUAGE_KEY&query=$searchKey&page=$page"
}

fun getDiscoverUrl(genreId: Int, languageKey: String?, startYear: String, endYear: String, page: Int): String? {
    var url = BASE_DISCOVER_URL + page
    if (startYear != "∞") url = "$url&primary_release_date.gte=$startYear-01-01"
    url = "$url&primary_release_date.lte=$endYear-12-31"
    if (genreId != 0) url = "$url&with_genres=$genreId"
    if (languageKey != null) url = "$url&with_original_language=$languageKey"
    return url
}