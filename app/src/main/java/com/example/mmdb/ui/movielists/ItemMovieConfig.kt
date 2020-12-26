package com.example.mmdb.ui.movielists

data class ItemMovieConfig(
    val position: Int,
    val page: Int? = null, //TODO not sure if it should actually be nullable (check offline lists)
    val onItemSelected: (() -> Unit)? = null,
    val onCustomListSelected: (() -> Unit)? = null,
    val onWatchlistSelected: (() -> Unit)? = null,
    val onDeleteSelected: (() -> Unit)? = null
)