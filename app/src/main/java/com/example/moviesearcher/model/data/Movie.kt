package com.example.moviesearcher.model.data

import com.example.moviesearcher.util.KEY_PRODUCTION_COUNTRIES
import com.example.moviesearcher.util.stringListToString
import com.google.gson.annotations.SerializedName

data class Movie(
        val id: Int,
        val title: String,

        @SerializedName("release_date")
        val releaseDate: String,

        @SerializedName("vote_average")
        val score: String,

        @SerializedName("genre_ids")
        val genreIds: List<Int>,

        val genres: List<Genre>,

        var genresString: String,

        @SerializedName(KEY_PRODUCTION_COUNTRIES)
        val countryList: List<Country>,
        var productionCountryString: String,

        val runtime: Int,
        val overview: String,

        @SerializedName("poster_path")
        val posterImageUrl: String,

        @SerializedName("backdrop_path")
        val backdropImageUrl: String
)

data class MovieResults(
        @SerializedName("results")
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