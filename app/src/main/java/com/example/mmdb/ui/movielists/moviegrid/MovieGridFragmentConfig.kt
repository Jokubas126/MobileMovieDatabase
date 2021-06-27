package com.example.mmdb.ui.movielists.moviegrid

import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.details.IdWrapper
import com.jokubas.mmdb.model.data.entities.MovieResults

data class MovieGridFragmentConfig(
    val provideMovies: suspend (movieListType: MovieListType, page: Int) -> MovieResults,
    val itemMovieEventListener: (idWrapper: IdWrapper) -> ItemMovieEventListener
)