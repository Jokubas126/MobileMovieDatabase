package com.jokubas.mmdb.model.data.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_MOVIE_GENRES_LIST

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