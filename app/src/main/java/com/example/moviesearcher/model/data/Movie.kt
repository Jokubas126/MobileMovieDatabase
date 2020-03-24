package com.example.moviesearcher.model.data

import android.graphics.Bitmap
import androidx.room.*
import com.example.moviesearcher.util.*
import com.google.gson.annotations.SerializedName

class MovieResults {

    @SerializedName(KEY_RESULT_LIST)
    var results = listOf<Movie>()

    @SerializedName(KEY_TOTAL_PAGES)
    var totalPages: Int = 0

    fun formatGenres(genres: Genres) {
        for (movie in results) {
            val genreList = mutableListOf<String>()
            for (id in movie.genreIds) {
                for (genre in genres.genreList) {
                    if (genre.id == id) {
                        genreList.add(genre.name)
                        break
                    }
                }
            }
            movie.genresString = stringListToString(genreList)
        }
    }
}

@Entity(tableName = "movie_list")
data class LocalMovieList(
    @PrimaryKey(autoGenerate = true) var roomId: Int,
    @ColumnInfo(name = "list_title") var listTitle: String?,
    @ColumnInfo(name = "movie_list_ids") var movieIdList: List<Int>?
) {
    constructor() : this(0, "", null)
}

@Entity(tableName = "movie")
data class Movie(

    @PrimaryKey(autoGenerate = true) var roomId: Int,

    @SerializedName(KEY_ID)
    @ColumnInfo(name = "movie_id") var remoteId: Int,

    var title: String,

    @ColumnInfo(name = "release_date")
    @SerializedName(KEY_MOVIE_RELEASE_DATE)
    var releaseDate: String,

    @ColumnInfo(name = "score")
    @SerializedName(KEY_MOVIE_SCORE)
    var score: String,

    @Ignore
    @SerializedName(KEY_MOVIE_GENRE_ID_LIST)
    val genreIds: List<Int>,

    @Ignore
    val genres: List<Genre>,

    @ColumnInfo(name = "genres_string")
    var genresString: String,

    @Ignore
    @SerializedName(KEY_MOVIE_PRODUCTION_COUNTRY_LIST)
    val productionCountryList: List<Country>,

    @ColumnInfo(name = "production_country_string")
    var productionCountryString: String,

    var runtime: Int,
    var overview: String,

    @SerializedName(KEY_MOVIE_POSTER_PATH)
    var posterImageUrl: String,

    @SerializedName(KEY_MOVIE_BACKDROP_PATH)
    var backdropImageUrl: String,

    @TypeConverters(BitmapTypeConverter::class)
    @ColumnInfo(name = "poster_image")
    var posterImageBitmap: Bitmap?,

    @TypeConverters(BitmapTypeConverter::class)
    @ColumnInfo(name = "backdrop_image")
    var backdropImageBitmap: Bitmap?,

    @Ignore
    var isInWatchlist: Boolean
) {

    fun finalizeInitialization(): Movie {
        genresString = stringListToString(getAnyNameList(genres))
        productionCountryString = stringListToString(getAnyNameList(productionCountryList))
        posterImageBitmap = imageUrlToBitmap(posterImageUrl)
        backdropImageBitmap = imageUrlToBitmap(backdropImageUrl)
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