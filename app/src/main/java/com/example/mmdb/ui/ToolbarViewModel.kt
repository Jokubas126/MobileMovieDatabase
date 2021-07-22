package com.example.mmdb.ui

import androidx.fragment.app.FragmentManager
import com.example.mmdb.config.ToolbarConfig
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.ui.drawer.DrawerAction

// TODO add toolbar button functionality, visibility and similar
class ToolbarViewModel(
    private val toolbarConfig: ToolbarConfig,
    private val navController: NavigationController
) {

    interface ClickListener {

        fun onConfirmClicked() {}
    }

    private var clickListener: ClickListener? = null

    val isBackButtonEnabled: Boolean = toolbarConfig.backButtonEnabled.get()

    val isBurgerMenuButtonEnabled: Boolean = toolbarConfig.drawerButtonEnabled.get()

    val isConfirmButtonEnabled: Boolean = toolbarConfig.confirmButtonEnabled.get()

    val onDrawerOpenClicked = { navController.goTo(DrawerAction.Open) }

    val onBackClicked = {
        if (isBackButtonEnabled) navController.goBack()
    }

    val onConfirmClicked = {
        clickListener?.onConfirmClicked()
    }

    fun setClickListener(listener: ClickListener){
        clickListener = listener
    }

    fun attachToNavigationController(
        fragmentManager: FragmentManager
    ) {
        navController.attachChildFragmentManager(
            fragmentManager = fragmentManager,
            onToolbarChanged = { isTopFragment ->
                if (isTopFragment)
                    toolbarConfig.setDrawerFragment()
            }
        )
    }
}