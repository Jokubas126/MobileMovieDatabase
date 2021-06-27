package com.example.mmdb.ui.movielists.moviegrid

import com.example.mmdb.navigation.actions.MovieListType
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.WatchlistMovie

data class MovieGridFragmentConfig(
    val provideMovies: suspend (movieListType: MovieListType, page: Int) -> MovieResults,
    val provideWatchlist: suspend () -> List<WatchlistMovie>,
    val itemMovieEventListener: (movieId: Int, isRemote: Boolean) -> ItemMovieEventListener
)