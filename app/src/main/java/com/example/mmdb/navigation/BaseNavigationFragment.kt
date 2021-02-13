package com.example.mmdb.navigation

import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.ui.ToolbarViewModel

abstract class BaseNavigationFragment: Fragment() {

    protected val appConfig: AppConfig by lazy {
        requireAppConfig()
    }

    protected val navController: NavigationController by lazy {
        requireNavController()
    }

    protected val toolbarViewModel: ToolbarViewModel by lazy {
        ToolbarViewModel(
            appConfig.toolbarConfig,
            appConfig.drawerConfig,
            navController
        )
    }
}