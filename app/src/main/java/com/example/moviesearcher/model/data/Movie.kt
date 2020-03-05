package com.example.moviesearcher.model.data

import com.google.gson.annotations.SerializedName

class Movie {
    var id = 0
    lateinit var title: String

    @SerializedName("release_date")
    lateinit var releaseDate: String

    @SerializedName("vote_average")
    lateinit var score: String

    @SerializedName("genre_ids")
    lateinit var genreIds: List<Int>

    lateinit var genresString: String
    //private lateinit var productionCountries: String
    //private var runtime = 0
    //private lateinit var description: String

    @SerializedName("poster_path")
    lateinit var posterImageUrl: String

    @SerializedName("backdrop_path")
    lateinit var backdropImageUrl: String
}