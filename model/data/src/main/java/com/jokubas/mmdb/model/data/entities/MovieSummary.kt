package com.jokubas.mmdb.model.data.entities

import com.jokubas.mmdb.model.data.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MovieSummary(
    @SerialName(KEY_ID)
    val id: Int = 0,

    val title: String? = null,

    @SerialName(KEY_MOVIE_RELEASE_DATE)
    val releaseDate: String? = null,

    @SerialName(KEY_MOVIE_SCORE)
    val score: String? = null,

    @SerialName(KEY_MOVIE_GENRE_ID_LIST)
    val genreIds: List<Int> = emptyList(),

    @SerialName(KEY_MOVIE_POSTER_URL)
    val posterImageUrl: String? = null,

    val posterImageUriString: String? = null,
) {
    var genres: List<Genre>? = null

    val genresString: String?
        get() = genres?.joinToString { it.name }
}

fun MovieSummary.mapGenres(availableGenres: List<Genre>) {
    genres = availableGenres.filter { genre ->
        genreIds.any { genre.id == it }
    }
}

fun Movie.toMovieSummary() = MovieSummary(
    id = id,
    title = title,
    releaseDate = releaseDate,
    score = score,
    genreIds = genres.map { it.id },
    posterImageUrl = posterImageUrl,
    posterImageUriString = posterImageUriString
)