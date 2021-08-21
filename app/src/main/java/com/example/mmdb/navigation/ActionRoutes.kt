package com.example.mmdb.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.example.mmdb.navigation.actions.*
import com.example.mmdb.navigation.configproviders.*
import com.example.mmdb.ui.about.AboutFragment
import com.example.mmdb.ui.about.AboutFragmentArgs
import com.jokubas.mmdb.moviedetails.ui.DetailsFragment
import com.jokubas.mmdb.moviedetails.ui.DetailsFragmentArgs
import com.jokubas.mmdb.moviedetails.ui.credits.CreditsFragment
import com.jokubas.mmdb.moviedetails.ui.credits.CreditsFragmentArgs
import com.jokubas.mmdb.moviedetails.ui.media.MediaFragment
import com.jokubas.mmdb.moviedetails.ui.media.MediaFragmentArgs
import com.jokubas.mmdb.moviedetails.ui.overview.OverviewFragment
import com.jokubas.mmdb.moviedetails.ui.overview.OverviewFragmentArgs
import com.jokubas.mmdb.moviediscover.ui.discover.DiscoverFragment
import com.jokubas.mmdb.moviediscover.ui.discover.DiscoverFragmentArgs
import com.jokubas.mmdb.moviegrid.ui.grid.MovieGridFragment
import com.jokubas.mmdb.moviegrid.ui.grid.MovieGridFragmentArgs
import com.jokubas.mmdb.moviedetails.actions.DetailsFragmentAction
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.moviegrid.actions.MovieGridFragmentAction

internal typealias FragmentProvider<T> = (action: T) -> Fragment

/*
 * Add here all your mapping between actions and their fragment providers:
 * . key: Action
 * . value: FragmentProvider<Action>: (Action) -> Fragment
 */
val actionRoutes: List<ActionFragmentProviderPair<*>> = listOf(
    ActionFragmentProviderPair(
        key = InnerDetailsAction.Overview::class.java,
        value = { action ->
            OverviewFragment().apply {
                arguments = OverviewFragmentArgs.create(
                    action = action,
                    configProvider = OverviewConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = InnerDetailsAction.Media::class.java,
        value = { action ->
            MediaFragment().apply {
                arguments = MediaFragmentArgs.create(
                    action = action,
                    configProvider = MediaConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = InnerDetailsAction.Credits::class.java,
        value = { action ->
            CreditsFragment().apply {
                arguments = CreditsFragmentArgs.create(
                    action = action,
                    configProvider = CreditsConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = DetailsFragmentAction::class.java,
        value = { action ->
            DetailsFragment().apply {
                arguments = DetailsFragmentArgs.create(
                    action = action,
                    configProvider = DetailsFragmentConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = MovieGridFragmentAction::class.java,
        value = { action ->
            MovieGridFragment().apply {
                arguments = MovieGridFragmentArgs.create(
                    action = action,
                    configProvider = MovieGridFragmentConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = com.jokubas.mmdb.moviediscover.actions.DiscoverFragmentAction::class.java,
        value = { action ->
            DiscoverFragment().apply {
                arguments = DiscoverFragmentArgs.create(
                    action = action,
                    configProvider = DiscoverFragmentConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = AboutFragmentAction::class.java,
        value = { action ->
            AboutFragment().apply {
                arguments = AboutFragmentArgs.create(
                    action = action,
                    configProvider = AboutFragmentConfigProvider::class.java
                )
            }
        }
    )
)

data class ActionFragmentProviderPair<T : Parcelable>(
    internal val key: Class<T>,
    internal val value: FragmentProvider<T>
)