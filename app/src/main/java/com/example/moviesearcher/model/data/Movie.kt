package com.example.moviesearcher.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
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
data class MovieList(
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "list_title") var listTitle: String?,
    /*@ColumnInfo(name = "movie_list")*/ @Ignore var movieList: List<Movie>?
) {
    constructor(): this(0, "default", null)
}

data class Movie(

    @PrimaryKey(autoGenerate = true) val dbId: Int,

    @ColumnInfo(name = "list_id")
    val movieListId: Int,

    @ColumnInfo(name = "movie_id") val id: Int,
    val title: String,

    @ColumnInfo(name = "release_date")
    @SerializedName(KEY_MOVIE_RELEASE_DATE)
    val releaseDate: String,

    @ColumnInfo(name = "movie_score")
    @SerializedName(KEY_MOVIE_SCORE)
    val score: String,

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

    val runtime: Int,
    val overview: String,

    @Ignore
    @SerializedName(KEY_MOVIE_POSTER_PATH)
    val posterImageUrl: String,

    @Ignore
    @SerializedName(KEY_MOVIE_BACKDROP_PATH)
    val backdropImageUrl: String
)