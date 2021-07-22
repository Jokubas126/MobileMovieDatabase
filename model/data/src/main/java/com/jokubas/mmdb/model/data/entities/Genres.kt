package com.jokubas.mmdb.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.KEY_MOVIE_GENRES_LIST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genres(
    @SerialName(KEY_MOVIE_GENRES_LIST)
    val genreList: List<Genre>
)

@Serializable
@Entity(tableName = "genre")
class Genre(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
)