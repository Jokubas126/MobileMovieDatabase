package com.example.mmdb.navigation.configproviders

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.DetailsFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.movielists.moviegrid.ItemMovieEventListener
import com.example.mmdb.ui.movielists.moviegrid.MovieGridFragmentConfig
import com.jokubas.mmdb.model.data.entities.MovieResults
import kotlin.math.min

const val MAX_MOVIES_FOR_PAGE = 20

class MovieGridFragmentConfigProvider : ConfigProvider<MovieGridFragmentConfig> {

    override fun config(fragment: Fragment): MovieGridFragmentConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val navController: NavigationController = fragment.requireNavController()

        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository
        val localMovieRepository = appConfig.movieConfig.roomMovieRepository
        val watchlistRepository = appConfig.movieConfig.watchlistRepository
        val customMovieListRepository = appConfig.movieConfig.customMovieListRepository

        return MovieGridFragmentConfig(
            provideMovies = { movieListType, page ->
                when (movieListType) {
                    MovieListType.Remote.Popular,
                    MovieListType.Remote.TopRated,
                    MovieListType.Remote.Upcoming,
                    MovieListType.Remote.NowPlaying -> {
                        remoteMovieRepository.getTypeMovies(
                            listType = movieListType.key,
                            page = page
                        )
                    }
                    is MovieListType.Remote.Discover -> {
                        with(movieListType) {
                            remoteMovieRepository.getDiscoveredMovies(
                                page = page,
                                startYear = startYear,
                                endYear = endYear,
                                genreKeys = genreKeys,
                                languageKeys = languageKeys
                            )
                        }
                    }
                    is MovieListType.Remote.Watchlist -> {
                        val watchlist = watchlistRepository.getWatchlist()
                        val watchlistMovies = remoteMovieRepository.getMoviesFromIdList(
                            watchlist.map { it.movieId }
                        )
                        MovieResults(
                            page = page,
                            movieList = watchlistMovies.subList(
                                page * MAX_MOVIES_FOR_PAGE - MAX_MOVIES_FOR_PAGE,
                                min(page * MAX_MOVIES_FOR_PAGE, watchlistMovies.size)
                            ),
                            totalPages = watchlistMovies.size / MAX_MOVIES_FOR_PAGE + 1
                        )
                    }
                    else -> {
                        remoteMovieRepository.getTypeMovies(
                            page = page
                        )
                    }
                }
            },
            provideWatchlist = {
                watchlistRepository.getWatchlist()
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
                    onWatchlistSelected = {
                        Toast.makeText(fragment.context, "Watchlist clicked", Toast.LENGTH_SHORT)
                            .show()
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