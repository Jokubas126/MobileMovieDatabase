package com.example.moviesearcher.model.data

import com.google.gson.annotations.SerializedName

data class Genres(
        @SerializedName("genres")
        val genreList: List<Genre>
)

class Genre(
        val id: Int,
        val name: String
)