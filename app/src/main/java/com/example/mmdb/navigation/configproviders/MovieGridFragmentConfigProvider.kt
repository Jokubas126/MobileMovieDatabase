package com.example.mmdb.navigation.configproviders

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.requireNavController
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.mapGenres
import com.jokubas.mmdb.moviedetails.actions.DetailsFragmentAction
import com.jokubas.mmdb.moviegrid.actions.MovieListType
import com.jokubas.mmdb.moviegrid.ui.grid.MovieGridFragmentConfig
import com.jokubas.mmdb.moviegrid.ui.movieitem.ItemMovieEventListener
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.navigationtools.ConfigProvider
import kotlinx.coroutines.flow.onEach

const val MAX_MOVIES_FOR_PAGE = 20

class MovieGridFragmentConfigProvider : ConfigProvider<MovieGridFragmentConfig> {

    override fun config(fragment: Fragment): MovieGridFragmentConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val navController: NavigationController = fragment.requireNavController()

        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository
        val remoteMovieListRepository = appConfig.movieConfig.remoteMovieListRepository
        val localMovieRepository = appConfig.movieConfig.roomMovieRepository
        val watchlistRepository = appConfig.movieConfig.watchlistRepository
        val customMovieListRepository = appConfig.movieConfig.customMovieListRepository
        val genresRepository = appConfig.movieConfig.genresRepository

        return MovieGridFragmentConfig(
            lifecycle = fragment.lifecycle,
            provideMovies = { movieListType, page ->
                when (movieListType) {
                    MovieListType.Remote.Popular,
                    MovieListType.Remote.TopRated,
                    MovieListType.Remote.Upcoming,
                    MovieListType.Remote.NowPlaying -> {
                        remoteMovieListRepository.typeMovies(
                            listType = movieListType.key,
                            pageFlow = page
                        )
                    }
                    is MovieListType.Remote.Discover -> {
                        with(movieListType) {
                            remoteMovieListRepository.discoveredMovies(
                                pageFlow = page,
                                startYear = startYear,
                                endYear = endYear,
                                genreKeys = genres,
                                languageKeys = languages
                            )
                        }
                    }
                    is MovieListType.Remote.Watchlist -> {
                        remoteMovieListRepository.moviesFromIdFlow(
                            idFlow = watchlistRepository.getWatchlistIdsFlow(),
                            pageFlow = page,
                            provideMovie = remoteMovieRepository.movieByIdNow,
                            maxPerPage = MAX_MOVIES_FOR_PAGE
                        )
                    }
                    else -> {
                        remoteMovieListRepository.typeMovies(
                            pageFlow = page
                        )
                    }
                }.onEach { response ->
                    (response as? DataResponse.Success<MovieResults>)?.value?.movieList?.forEach {
                        it.mapGenres(genresRepository.getAllGenres())
                    }
                }
            },
            provideWatchlist = {
                watchlistRepository.getWatchlistFlow()
            },
            itemMovieEventListener = { movieId, isRemote ->
                ItemMovieEventListener(
                    onItemSelected = {
                        navController.goTo(
                            action = DetailsFragmentAction(
                                movieId = movieId,
                                isRemote = isRemote
                            ),
                            animation = NavigationController.Animation.FromBottom,
                            useWrapper = false
                        )
                    },
                    onCustomListSelected = {
                        Toast.makeText(fragment.context, "Custom list clicked", Toast.LENGTH_SHORT)
                            .show()
                        //onPlaylistAddCLicked(movie) // TODO implement this
                    },
                    onWatchlistSelected = { isInWatchlist ->
                        if (isInWatchlist)
                            watchlistRepository.deleteWatchlistMovie(id = movieId)
                        else
                            watchlistRepository.insertOrUpdateMovie(id = movieId)
                    },
                    onDeleteSelected = {
                        //        gridAdapter.movieList.removeAt(position)
                        //        gridAdapter.notifyItemRemoved(position)
                        //        var restored = false
                        //        Snackbar.make(view, R.string.movie_deleted, Snackbar.LENGTH_LONG)
                        //            .setAction(R.string.undo) {
                        //                restored = true
                        //                gridAdapter.movieList.add(position, movie)
                        //                gridAdapter.notifyItemInserted(position)
                        //            }.show()
                        //
                        //        Handler().postDelayed({
                        //            if (!restored)
                        //                viewModel.deleteMovie(movie)
                        //        }, SNACKBAR_LENGTH_LONG_MS.toLong())
                    }
                )
            }
        )
    }
}