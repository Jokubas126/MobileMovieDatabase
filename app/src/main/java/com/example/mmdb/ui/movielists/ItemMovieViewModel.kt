package com.example.mmdb.ui.movielists

import com.jokubas.mmdb.model.data.entities.Movie

data class ItemMovieViewModel(
    val movie: Movie,
    val position: Int,
    val eventListener: ItemMovieEventListener,
    val page: Int? = null //TODO not sure if it should actually be nullable (check offline lists)
)

data class ItemMovieEventListener(
    val onItemSelected: (() -> Unit)? = null,
    val onCustomListSelected: (() -> Unit)? = null,
    val onWatchlistSelected: (() -> Unit)? = null,
    val onDeleteSelected: (() -> Unit)? = null
)

fun Movie.toItemMovieViewModel(
    position: Int,
    itemMovieEventListener: ItemMovieEventListener,
    page: Int? = null
) = ItemMovieViewModel(
    movie = this,
    position = position,
    eventListener = itemMovieEventListener,
    page = page
)