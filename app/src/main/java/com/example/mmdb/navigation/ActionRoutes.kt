package com.example.mmdb.navigation

import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.example.mmdb.ui.about.AboutFragment
import com.example.mmdb.ui.about.AboutFragmentArgs
import com.example.mmdb.navigation.actions.AboutFragmentAction
import com.example.mmdb.navigation.configproviders.AboutFragmentConfigProvider

internal typealias FragmentProvider<T> = (action: T) -> Fragment

/*
 * Add here all your mapping between actions and their fragment providers:
 * . key: Action
 * . value: FragmentProvider<Action>: (Action) -> Fragment
 */
val actionRoutes: List<ActionFragmentProviderPair<*>> = listOf(
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
    internal val screenDecoration: ScreenDecoration = ScreenDecoration.None
)