package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.*
import com.jokubas.mmdb.model.data.util.*
import com.jokubas.mmdb.util.extensions.urlToFileUriString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @SerialName(KEY_ID)
    val id: Int = 0,

    val title: String? = null,

    @ColumnInfo(name = KEY_MOVIE_RELEASE_DATE)
    @SerialName(KEY_MOVIE_RELEASE_DATE)
    val releaseDate: String? = null,

    @ColumnInfo(name = KEY_LOCAL_MOVIE_SCORE)
    @SerialName(KEY_MOVIE_SCORE)
    val score: String? = null,

    @ColumnInfo(name = KEY_MOVIE_GENRE_ID_LIST)
    @SerialName(KEY_MOVIE_GENRE_ID_LIST)
    val genreIds: List<Int> = emptyList(),

    @ColumnInfo(name = KEY_MOVIE_PRODUCTION_COUNTRY_LIST)
    @SerialName(KEY_MOVIE_PRODUCTION_COUNTRY_LIST)
    val productionCountryList: List<Country> = emptyList(),

    val runtime: Int = 0,

    @SerialName(KEY_MOVIE_DESCRIPTION)
    val description: String? = null,

    @SerialName(KEY_MOVIE_POSTER_URL)
    val posterImageUrl: String? = null,

    @SerialName(KEY_MOVIE_BACKDROP_URL)
    val backdropImageUrl: String? = null,

    @ColumnInfo(name = KEY_MOVIE_POSTER_URI_STRING)
    var posterImageUriString: String? = null,

    @ColumnInfo(name = KEY_MOVIE_BACKDROP_URI_STRING)
    var backdropImageUriString: String? = null
) {
    @Ignore
    var genres: List<Genre>? = null

    val genresString: String?
        get() = genres?.joinToString { it.name }

    @Ignore
    val productionCountriesListed: String =
        productionCountryList.joinToString(separator = "\n") { it.name }
}

fun Movie.mapGenres(availableGenres: List<Genre>) {
    genres = availableGenres.filter { genre ->
        genreIds.any { genre.id == it }
    }
}

fun Movie.saveMainImages(context: Context) {
    posterImageUriString = context.urlToFileUriString(posterImageUrl)
    backdropImageUriString = context.urlToFileUriString(backdropImageUrl)
}