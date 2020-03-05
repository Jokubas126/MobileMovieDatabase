package com.example.moviesearcher.model.data

import com.google.gson.annotations.SerializedName

class Genres {
    @SerializedName("genres")
    val genreList: List<Genre> = listOf()

    inner class Genre {
        var id = 0
        lateinit var name: String
    }
}