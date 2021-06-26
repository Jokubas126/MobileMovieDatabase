package com.example.mmdb.navigation.actions

import android.os.Parcelable
import com.jokubas.mmdb.util.constants.*
import kotlinx.android.parcel.Parcelize

@Parcelize
class MovieGridFragmentAction(
    val movieListType: MovieListType = MovieListType.Popular
) : WrappedFragmentAction

sealed class MovieListType(val key: String = KEY_DEFAULT) : Parcelable {

    @Parcelize
    object Popular : MovieListType(KEY_POPULAR)

    @Parcelize
    object TopRated : MovieListType(KEY_TOP_RATED)

    @Parcelize
    object NowPlaying : MovieListType(KEY_NOW_PLAYING)

    @Parcelize
    object Upcoming : MovieListType(KEY_UPCOMING)

    @Parcelize
    data class Discover(
        val startYear: String? = null,
        val endYear: String? = null,
        val genreKeys: List<String?> = listOf(),
        val languageKeys: List<String?> = listOf()
    ) : MovieListType() {

        val discoverNameList =
            listOf(
                startYear?.let { "From: $startYear" },
                "To: $endYear"
            )
                .plus(genreKeys)
                .plus(languageKeys)
    }

    @Parcelize
    data class Search(val searchQuery: String? = null) : MovieListType()

    //TODO implement local, custom list and watchlist types
}