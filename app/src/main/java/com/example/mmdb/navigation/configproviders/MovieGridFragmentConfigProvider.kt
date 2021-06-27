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
import com.example.mmdb.ui.movielists.ItemMovieEventListener
import com.example.mmdb.ui.movielists.rest.MovieGridFragmentConfig

class MovieGridFragmentConfigProvider : ConfigProvider<MovieGridFragmentConfig> {
    override fun config(fragment: Fragment): MovieGridFragmentConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val navController: NavigationController = fragment.requireNavController()

        val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository
        val movieRepository = appConfig.movieConfig.roomMovieRepository
        val customMovieListRepository = appConfig.movieConfig.customMovieListRepository
        val watchlistRepository = appConfig.movieConfig.watchlistRepository

        return MovieGridFragmentConfig(
            provideMovies = { movieListType, page ->
                when (movieListType) {
                    MovieListType.Remote.Popular -> {
                        remoteMovieRepository.getTypeMovies(
                            movieListType.key,
                            page
                        )
                    }
                    else -> {
                        remoteMovieRepository.getTypeMovies(
                            movieListType.key,
                            page
                        )
                    }
                }
            },
            itemMovieEventListener = { idWrapper ->
                ItemMovieEventListener(
                    onItemSelected = {
                        navController.goTo(
                            action = DetailsFragmentAction(
                                idWrapper = idWrapper
                            ),
                            animation = NavigationController.Animation.FromBottom,
                            useWrapper = false
                        )
                    },
                    onCustomListSelected = {
                        Toast.makeText(fragment.context, "Playlist clicked", Toast.LENGTH_SHORT)
                            .show()
                        //onPlaylistAddCLicked(movie) // TODO implement this
                    },
                    onWatchlistSelected = {
                        //updateWatchlist(movie)
                    }
                )
            }
        )
    }
}