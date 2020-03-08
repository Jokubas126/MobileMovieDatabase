package com.example.moviesearcher.model.data

import com.example.moviesearcher.util.*
import com.google.gson.annotations.SerializedName

data class Movie(
        val id: Int,
        val title: String,

        @SerializedName(KEY_MOVIE_RELEASE_DATE)
        val releaseDate: String,

        @SerializedName(KEY_MOVIE_SCORE)
        val score: String,

        @SerializedName(KEY_MOVIE_GENRE_ID_LIST)
        val genreIds: List<Int>,

        val genres: List<Genre>,

        var genresString: String,

        @SerializedName(KEY_MOVIE_PRODUCTION_COUNTRY_LIST)
        val countryList: List<Country>,
        var productionCountryString: String,

        val runtime: Int,
        val overview: String,

        @SerializedName(KEY_MOVIE_POSTER_PATH)
        val posterImageUrl: String,

        @SerializedName(KEY_MOVIE_BACKDROP_PATH)
        val backdropImageUrl: String
)

data class MovieResults(
        @SerializedName(KEY_TOTAL_PAGES)
        var totalPages: Int,

        @SerializedName(KEY_RESULT_LIST)
        var results: List<Movie>
) {


    fun formatGenres(genres: Genres) {
        for (movie in results) {
            val genreList = mutableListOf<String>()
            for (id in movie.genreIds) {
                for (genre in genres.genreList) {
                    if (genre.id == id) {
                        genreList.add(genre.name)
                        break
                    }
                }
            }
            movie.genresString = stringListToString(genreList)
        }
    }
}