package com.example.mmdb.ui.movielists

data class ItemMovieConfig(
    val onItemSelected: (() -> Unit)? = null,
    val onCustomListSelected: (() -> Unit)? = null,
    val onWatchlistSelected: (() -> Unit)? = null,
    val onDeleteSelected: (() -> Unit)? = null
)