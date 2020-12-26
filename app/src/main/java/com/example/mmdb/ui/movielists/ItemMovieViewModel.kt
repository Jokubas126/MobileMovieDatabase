package com.example.mmdb.ui.movielists

import com.jokubas.mmdb.model.data.entities.Movie

class ItemMovieViewModel(
    val movie: Movie,
    val itemMovieConfig: ItemMovieConfig
)

fun Movie.toItemMovieViewModel(itemMovieConfig: ItemMovieConfig) =
    ItemMovieViewModel(this, itemMovieConfig)