package com.example.mmdb.ui

import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.ui.drawer.DrawerAction

// TODO add toolbar button functionality, visibility and similar
class ToolbarViewModel(
    private val navController: NavigationController
) {

    val onDrawerOpenClicked = { navController.goTo(DrawerAction.Open) }
}