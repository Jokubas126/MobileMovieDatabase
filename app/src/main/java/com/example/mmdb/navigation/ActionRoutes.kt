package com.example.mmdb.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.example.mmdb.ui.about.AboutFragment
import com.example.mmdb.ui.about.AboutFragmentArgs
import com.example.mmdb.navigation.actions.AboutFragmentAction
import com.example.mmdb.navigation.actions.DiscoverFragmentAction
import com.example.mmdb.navigation.configproviders.AboutFragmentConfigProvider
import com.example.mmdb.navigation.configproviders.RemoteMovieGridFragmentConfigProvider
import com.example.mmdb.ui.movielists.rest.RemoteMovieGridFragment
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction
import com.example.mmdb.navigation.configproviders.DiscoverFragmentConfigProvider
import com.example.mmdb.ui.discover.DiscoverFragment
import com.example.mmdb.ui.discover.DiscoverFragmentArgs
import com.example.mmdb.ui.movielists.rest.RemoteMovieGridFragmentArgs

internal typealias FragmentProvider<T> = (action: T) -> Fragment

/*
 * Add here all your mapping between actions and their fragment providers:
 * . key: Action
 * . value: FragmentProvider<Action>: (Action) -> Fragment
 */
val actionRoutes: List<ActionFragmentProviderPair<*>> = listOf(
    ActionFragmentProviderPair(
        key = RemoteMovieGridFragmentAction::class.java,
        value = { action ->
            RemoteMovieGridFragment().apply {
                arguments = RemoteMovieGridFragmentArgs.create(
                    action = action,
                    configProvider = RemoteMovieGridFragmentConfigProvider::class.java
                )
            }
        }
    ),
    ActionFragmentProviderPair(
        key = DiscoverFragmentAction::class.java,
        value = { action ->
            DiscoverFragment().apply {
                arguments = DiscoverFragmentArgs.create(
                    action = action,
                    configProvider = DiscoverFragmentConfigProvider::class.java
                )
            }
        },
        screenDecoration = ScreenDecoration.NoDrawer
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
    internal val value: FragmentProvider<T>,
    internal val screenDecoration: ScreenDecoration = ScreenDecoration.WithDrawer
)