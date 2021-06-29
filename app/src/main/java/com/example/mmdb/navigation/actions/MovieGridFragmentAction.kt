package com.example.mmdb.navigation.actions

import android.os.Parcelable
import com.jokubas.mmdb.util.constants.*
import kotlinx.android.parcel.Parcelize

@Parcelize
class MovieGridFragmentAction(
    val movieListType: MovieListType = MovieListType.Remote.Popular
) : WrappedFragmentAction

sealed class MovieListType(val key: String = KEY_DEFAULT) : Parcelable {

    sealed class Remote(key: String = KEY_DEFAULT) : MovieListType(key) {

        @Parcelize
        object Popular : Remote(KEY_POPULAR)

        @Parcelize
        object TopRated : Remote(KEY_TOP_RATED)

        @Parcelize
        object NowPlaying : Remote(KEY_NOW_PLAYING)

        @Parcelize
        object Upcoming : Remote(KEY_UPCOMING)

        @Parcelize
        data class Discover(
            val startYear: String? = null,
            val endYear: String? = null,
            val genreKeys: List<String?> = listOf(),
            val languageKeys: List<String?> = listOf()
        ) : Remote() {

            val discoverNameList =
                listOf(
                    startYear?.let { "From: $startYear" },
                    "To: $endYear"
                )
                    .plus(genreKeys)
                    .plus(languageKeys)
        }

        @Parcelize
        data class Search(val searchQuery: String? = null) : Remote()

        @Parcelize
        object Watchlist: Remote()
    }

    sealed class Local(key: String = KEY_DEFAULT) : MovieListType(key) {

        @Parcelize
        object CustomList: Local()
    }
}