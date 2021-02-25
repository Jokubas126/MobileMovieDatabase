package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.example.mmdb.ui.details.DetailsFragmentConfig

class DetailsFragmentConfigProvider : ConfigProvider<DetailsFragmentConfig> {


    override fun config(fragment: Fragment): DetailsFragmentConfig {
        val navController = fragment.requireNavController()

        return DetailsFragmentConfig(
            onBottomNavigationAction = { action ->
                navController.goTo(
                    action = action
                )
            }
        )
    }
}