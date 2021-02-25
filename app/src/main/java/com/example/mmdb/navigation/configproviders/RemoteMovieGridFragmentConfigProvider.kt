package com.example.mmdb.navigation.configproviders

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.DetailsFragmentAction
import com.example.mmdb.ui.details.DetailsMovieId
import com.example.mmdb.ui.movielists.ItemMovieConfig
import com.example.mmdb.ui.movielists.rest.RemoteMovieGridFragmentConfig

class RemoteMovieGridFragmentConfigProvider : ConfigProvider<RemoteMovieGridFragmentConfig> {
    override fun config(fragment: Fragment): RemoteMovieGridFragmentConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val navController: NavigationController = fragment.requireNavController()

        return RemoteMovieGridFragmentConfig(
            itemMovieConfig = { page, position, movie ->
                ItemMovieConfig(
                    position = position,
                    page = page,
                    onItemSelected = {
                        //onMovieClicked(movie.remoteId)
                        navController.goTo(
                            action = DetailsFragmentAction(
                                movieId = DetailsMovieId.Remote(movie.remoteId)
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