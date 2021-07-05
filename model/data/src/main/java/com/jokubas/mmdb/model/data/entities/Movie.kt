package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.*

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerializedName(KEY_ID)
    var id: Int,

    var title: String?,

    @ColumnInfo(name = KEY_MOVIE_RELEASE_DATE)
    @SerializedName(KEY_MOVIE_RELEASE_DATE)
    var releaseDate: String?,

    @ColumnInfo(name = KEY_LOCAL_MOVIE_SCORE)
    @SerializedName(KEY_MOVIE_SCORE)
    var score: String?,

    @Ignore
    @SerializedName(KEY_MOVIE_GENRE_ID_LIST)
    val genreIds: List<Int>,

    @Ignore
    val genres: List<Genre>,

    @ColumnInfo(name = KEY_MOVIE_GENRES_STRING)
    var genresString: String?,

    @Ignore
    @SerializedName(KEY_MOVIE_PRODUCTION_COUNTRY_LIST)
    val productionCountryList: List<Country>,

    @ColumnInfo(name = KEY_MOVIE_PRODUCTION_COUNTRY_STRING)
    var productionCountryString: String?,

    var runtime: Int,

    @SerializedName(KEY_MOVIE_DESCRIPTION)
    var description: String?,

    @SerializedName(KEY_MOVIE_POSTER_URL)
    var posterImageUrl: String?,

    @SerializedName(KEY_MOVIE_BACKDROP_URL)
    var backdropImageUrl: String?,

    @ColumnInfo(name = KEY_MOVIE_POSTER_URI_STRING)
    var posterImageUriString: String?,

    @ColumnInfo(name = KEY_MOVIE_BACKDROP_URI_STRING)
    var backdropImageUriString: String?
) {

    @Ignore
    val productionCountriesListed = productionCountryList.joinToString(separator = "\n")

    fun formatGenresString(genreList: List<Genre>) {
        genresString = genreList.joinToString { it.name }
    }

    fun finalizeInitialization(context: Context): Movie {

        genresString = genres.joinToString { it.name }
        productionCountryString = productionCountryList.joinToString{ it.name }
        posterImageUriString = imageUrlToFileUriString(context, posterImageUrl)
        backdropImageUriString = imageUrlToFileUriString(context, backdropImageUrl)
        return this
    }

    constructor() : this(
        0,
        "",
        "",
        "",
        emptyList(),
        emptyList(),
        "",
        emptyList(),
        "",
        0, "",
        "",
        "",
        null,
        null
    )
}