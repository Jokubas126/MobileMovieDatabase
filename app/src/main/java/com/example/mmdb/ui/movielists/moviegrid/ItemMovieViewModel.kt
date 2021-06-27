package com.example.mmdb.ui.movielists.moviegrid

import com.jokubas.mmdb.model.data.entities.Movie

data class ItemMovieViewModel(
    val movie: Movie,
    val position: Int,
    val eventListener: ItemMovieEventListener,
    val page: Int? = null,//TODO not sure if it should actually be nullable (check offline lists)
    val isInWatchlist: Boolean
)

fun Movie.toItemMovieViewModel(
    position: Int,
    itemMovieEventListener: ItemMovieEventListener,
    page: Int? = null,
    isInWatchlist: Boolean
) = ItemMovieViewModel(
    movie = this,
    position = position,
    eventListener = itemMovieEventListener,
    page = page,
    isInWatchlist = isInWatchlist
)