package com.example.moviesearcher.util

// GENERAL
const val MOVIE_DB_API_KEY = "c6c88c6e91d206e35fa6aff0b9d1cc36"
const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w780"
const val MOVIE_DB_LANGUAGE_EN = "en-US"
const val MOVIE_DB_IMAGE_LANGUAGE_EN = "en"
const val KEY_RESULT_LIST = "results"
const val KEY_ID = "id"
const val KEY_NAME = "name"
const val KEY_ENGLISH_NAME = "english_name"
const val KEY_TOTAL_PAGES = "total_pages"

// MOVIE LIST TYPES
const val KEY_POPULAR = "popular"
const val KEY_TOP_RATED = "top_rated"
const val KEY_NOW_PLAYING = "now_playing"
const val KEY_UPCOMING = "upcoming"

// QUERIES
const val QUERY_API_KEY = "api_key"
const val QUERY_LANGUAGE = "language"
const val QUERY_PAGE = "page"
const val QUERY_RELEASE_DATE_START = "primary_release_date.gte"
const val QUERY_RELEASE_DATE_END = "primary_release_date.lte"
const val QUERY_GENRES = "with_genres"
const val QUERY_ORIGINAL_LANGUAGE = "with_original_language"
const val QUERY_SEARCH_QUERY = "query"

// PATHS
const val PATH_MOVIE_ID = "movie_id"
const val PATH_MOVIE_LIST_TYPE = "list_type"

// MOVIE LIST
const val KEY_LIST_TITLE = "list_title"
const val KEY_MOVIE_LIST_IDS = "movie_list_ids"

// MOVIE
const val KEY_MOVIE_ID = "movie_id"
const val KEY_MOVIE_TITLE = "title"
const val KEY_MOVIE_RELEASE_DATE = "release_date"
const val KEY_MOVIE_SCORE = "vote_average"
const val KEY_LOCAL_MOVIE_SCORE = "score"
const val KEY_MOVIE_RUNTIME = "runtime"
const val KEY_MOVIE_DESCRIPTION = "overview"
const val KEY_MOVIE_POSTER_URL = "poster_path"
const val KEY_MOVIE_POSTER_URI_STRING = "poster_image_uri_string"
const val KEY_MOVIE_BACKDROP_URL = "backdrop_path"
const val KEY_MOVIE_BACKDROP_URI_STRING = "backdrop_image_uri_string"
const val KEY_MOVIE_PRODUCTION_COUNTRY_LIST = "production_countries"
const val KEY_MOVIE_PRODUCTION_COUNTRY_STRING = "production_country_string"

// GENRES
const val KEY_MOVIE_GENRE_ID_LIST = "genre_ids"
const val KEY_MOVIE_GENRES_LIST = "genres"
const val KEY_MOVIE_GENRES_STRING = "genres_string"

// CAST AND CREW
const val KEY_CAST_LIST = "cast"
const val KEY_CAST_POSITION = "character"
const val KEY_CREW_LIST = "crew"
const val KEY_CREW_POSITION = "job"
const val KEY_PROFILE_IMAGE_URL = "profile_path"
const val KEY_PROFILE_IMAGE_URI_STRING = "profile_image_uri_string"

// IMAGES
const val KEY_POSTER_LIST = "posters"
const val KEY_LOCAL_POSTER_LIST = "poster_list"
const val KEY_BACKDROP_LIST = "backdrops"
const val KEY_LOCAL_BACKDROP_LIST = "backdrop_list"
const val KEY_IMAGE_URL = "file_path"
const val KEY_IMAGE_URI_STRING = "image_uri_string"

// TRAILER
const val KEY_VIDEO_SITE = "site"
const val KEY_YOUTUBE_SITE = "YouTube"
const val KEY_VIDEO_TYPE = "type"
const val KEY_TRAILER_TYPE = "Trailer"

// ISO CODES
const val KEY_COUNTRY_ISO_CODE = "iso_3166_1"
const val KEY_LANGUAGE_ISO_CODE = "iso_639_1"