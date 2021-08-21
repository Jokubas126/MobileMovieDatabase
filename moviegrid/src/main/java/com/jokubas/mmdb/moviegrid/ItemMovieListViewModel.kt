package com.jokubas.mmdb.moviegrid

import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.moviegrid.actions.MovieListType

data class ItemMovieListViewModel(
    val movieListType: MovieListType,
    val itemMovieEventListener: (movieId: Int) -> ItemMovieEventListener,
    val watchlistMovieIds: List<Int>,
    val movieResults: MovieResults
) {

    val itemMovieViewModels: List<ItemMovieViewModel> =
        movieResults.movieList.mapIndexed { index, movieSummary ->
            movieSummary.toItemMovieViewModel(
                position = index,
                itemMovieEventListener = itemMovieEventListener.invoke(movieSummary.id),
                page = movieResults.page,
                isInWatchlist = watchlistMovieIds.any {
                    it == movieSummary.id
                },
                isRemote = movieListType is MovieListType.Remote
            )
        }
}
