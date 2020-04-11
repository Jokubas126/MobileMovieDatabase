package com.example.moviesearcher.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.moviesearcher.util.KEY_MOVIE_GENRES_LIST
import com.google.gson.annotations.SerializedName

data class Genres(
        @SerializedName(KEY_MOVIE_GENRES_LIST)
        val genreList: List<Genre>
)

@Entity(tableName = "genre")
class Genre(
        @PrimaryKey(autoGenerate = false)
        val id: Int,
        val name: String
)