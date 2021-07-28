package com.example.mmdb.ui.movielists.moviegrid

import androidx.lifecycle.Lifecycle
import com.example.mmdb.navigation.actions.MovieListType
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

data class MovieGridFragmentConfig constructor(
    val lifecycle: Lifecycle,
    val provideMovies: suspend (movieListType: MovieListType, page: StateFlow<Int>) -> Flow<DataResponse<MovieResults>>,
    val provideWatchlist: suspend () -> Flow<List<WatchlistMovie>>,
    val itemMovieEventListener: (movieId: Int, isRemote: Boolean) -> ItemMovieEventListener
)