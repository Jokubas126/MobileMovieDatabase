package com.example.mmdb.ui

import androidx.fragment.app.FragmentManager
import com.example.mmdb.config.AppConfig
import com.example.mmdb.config.DrawerConfig
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.ui.drawer.DrawerAction

// TODO add toolbar button functionality, visibility and similar
class ToolbarViewModel(
    private val drawerConfig: DrawerConfig,
    private val navController: NavigationController
) {

    val drawerEnabled: Boolean = drawerConfig.isDrawerEnabled

    val onDrawerOpenClicked = { navController.goTo(DrawerAction.Open) }

    val onBackClicked = { navController.goBack() }

    fun attachToNavigationController(
        fragmentManager: FragmentManager
    ) {
        navController.attachChildFragmentManager(
            fragmentManager = fragmentManager,
            onToolbarChanged = {

            }
        )
    }
}