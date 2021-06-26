package com.example.mmdb.ui.movielists.rest

import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.details.IdWrapper
import com.example.mmdb.ui.movielists.ItemMovieEventListener
import com.example.mmdb.ui.movielists.ItemMovieViewModel
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieResults

data class MovieGridFragmentConfig(
    val provideMovies: suspend (movieListType: MovieListType, page: Int) -> MovieResults,
    val itemMovieEventListener: (idWrapper: IdWrapper) -> ItemMovieEventListener
)