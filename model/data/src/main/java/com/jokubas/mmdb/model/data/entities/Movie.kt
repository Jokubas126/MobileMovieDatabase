package com.jokubas.mmdb.model.data.entities

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jokubas.mmdb.model.data.util.*
import com.jokubas.mmdb.util.stringListToString
import java.util.*

class MovieResults {

    @SerializedName(KEY_RESULT_LIST)
    var movieList = listOf<Movie>()

    @SerializedName(KEY_TOTAL_PAGES)
    var totalPages: Int = 0
}

@Entity(tableName = "movie_list")
data class CustomMovieList(
    @PrimaryKey(autoGenerate = true)
    var roomId: Int,

    @ColumnInfo(name = KEY_UPDATE_DATE)
    var updateDate: Date?,

    @ColumnInfo(name = KEY_LIST_TITLE)
    var listTitle: String?,

    @ColumnInfo(name = KEY_MOVIE_LIST_IDS)
    var movieIdList: List<Int>?
) {
    constructor() : this(0, Date(), "", null)
}

@Entity(tableName = "watchlist_movie")
data class WatchlistMovie(
    @PrimaryKey(autoGenerate = false) var movieId: Int
)

@Entity(tableName = "movie")
data class Movie(

    @PrimaryKey(autoGenerate = true) var roomId: Int,

    @SerializedName(KEY_ID)
    @ColumnInfo(name = KEY_MOVIE_ID) var remoteId: Int,

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
    var backdropImageUriString: String?,

    @Ignore
    var isInWatchlist: Boolean
) {

    fun formatGenresString(genreList: List<Genre>) {
        genresString = stringListToString(getAnyNameList(genreList))
    }

    fun finalizeInitialization(context: Context): Movie {
        genresString = stringListToString(getAnyNameList(genres))
        productionCountryString = stringListToString(getAnyNameList(productionCountryList))
        posterImageUriString = imageUrlToFileUriString(context, posterImageUrl)
        backdropImageUriString = imageUrlToFileUriString(context, backdropImageUrl)
        return this
    }

    constructor() : this(
        0,
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
        null,
        false
    )
}