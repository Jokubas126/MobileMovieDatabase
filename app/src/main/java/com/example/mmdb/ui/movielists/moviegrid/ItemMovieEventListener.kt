package com.example.mmdb.ui.movielists.moviegrid

data class ItemMovieEventListener(
    val onItemSelected: (() -> Unit)? = null,
    val onCustomListSelected: (() -> Unit)? = null,
    val onWatchlistSelected: (() -> Unit)? = null,
    val onDeleteSelected: (() -> Unit)? = null
)