package com.jokubas.mmdb.moviegrid.actions

import android.os.Parcelable
import com.jokubas.mmdb.util.constants.*
import kotlinx.parcelize.Parcelize

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
            val genres: List<String> = emptyList(),
            val languages: List<String> = emptyList(),
            val discoverNameList: List<String> = emptyList()
        ) : Remote()

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