package com.example.mmdb.ui.movielists.moviegrid

import androidx.databinding.ObservableBoolean
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieSummary

data class ItemMovieViewModel(
    val movie: MovieSummary,
    val position: Int,
    val eventListener: ItemMovieEventListener,
    val page: Int? = null,//TODO not sure if it should actually be nullable (check offline lists)
    val isInWatchlist: ObservableBoolean,
    val isRemote: Boolean
) {

    fun onWatchlistClicked() {
        eventListener.onWatchlistSelected?.invoke(isInWatchlist.get())
    }
}

fun MovieSummary.toItemMovieViewModel(
    position: Int,
    itemMovieEventListener: ItemMovieEventListener,
    page: Int? = null,
    isInWatchlist: Boolean,
    isRemote: Boolean
) = ItemMovieViewModel(
    movie = this,
    position = position,
    eventListener = itemMovieEventListener,
    page = page,
    isInWatchlist = ObservableBoolean(isInWatchlist),
    isRemote = isRemote
)