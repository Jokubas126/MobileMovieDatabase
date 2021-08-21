package com.jokubas.mmdb.moviegrid

data class ItemMovieEventListener(
    val onItemSelected: (() -> Unit)? = null,
    val onCustomListSelected: (() -> Unit)? = null,
    val onWatchlistSelected: ((isInWatchlist: Boolean) -> Unit)? = null,
    val onDeleteSelected: (() -> Unit)? = null
)