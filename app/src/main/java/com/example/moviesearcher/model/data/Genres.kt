package com.example.moviesearcher.model.data

import com.example.moviesearcher.util.KEY_MOVIE_GENRES_LIST
import com.google.gson.annotations.SerializedName

data class Genres(
        @SerializedName(KEY_MOVIE_GENRES_LIST)
        val genreList: List<Genre>
)

class Genre(
        val id: Int,
        val name: String
)