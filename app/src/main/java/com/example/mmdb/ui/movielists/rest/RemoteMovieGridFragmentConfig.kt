package com.example.mmdb.ui.movielists.rest

import com.example.mmdb.ui.movielists.ItemMovieConfig
import com.jokubas.mmdb.model.data.entities.Movie

data class RemoteMovieGridFragmentConfig(
    val itemMovieConfig: (page: Int, position: Int, movie: Movie) -> ItemMovieConfig
)