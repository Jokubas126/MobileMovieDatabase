package com.example.mmdb.ui.movielists

import com.example.mmdb.R
import com.jokubas.mmdb.model.data.entities.Movie

class ItemMovieViewModel(
    val movie: Movie,
    val itemMovieConfig: ItemMovieConfig
) {
    val fullStarDrawableResId = R.drawable.ic_star_full
    val emptyStarDrawableResId = R.drawable.ic_star_empty
}

fun List<Movie>.toItemMovieViewModels(itemMovieConfig: ItemMovieConfig): List<ItemMovieViewModel> {
    val viewModels = mutableListOf<ItemMovieViewModel>()
    forEach { movie ->
        viewModels.add(ItemMovieViewModel(movie, itemMovieConfig))
    }
    return viewModels
}

fun Movie.toItemMovieViewModel(itemMovieConfig: ItemMovieConfig) =
    ItemMovieViewModel(this, itemMovieConfig)