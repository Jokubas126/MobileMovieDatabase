package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mmdb.R
import com.jokubas.mmdb.util.navigationtools.ConfigProvider
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.requireNavController
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.moviedetails.ui.DetailsFragmentConfig

class DetailsFragmentConfigProvider : ConfigProvider<DetailsFragmentConfig> {

    override fun config(fragment: Fragment): DetailsFragmentConfig {

        val navController = fragment.requireNavController()

        return DetailsFragmentConfig(
            loadInitialView = { idWrapper, isRemote ->
                navController.resolveFragment(InnerDetailsAction.Overview(idWrapper, isRemote))
                    ?.let { resolvedFragment ->
                        handleDetailsFragment(
                            fragmentManager = fragment.childFragmentManager,
                            fragment = resolvedFragment
                        )
                    }
            },
            onBottomNavigationAction = { action ->
                navController.resolveFragment(action)?.let { resolvedFragment ->
                    handleDetailsFragment(
                        fragmentManager = fragment.childFragmentManager,
                        fragment = resolvedFragment,
                        animation = null //TODO figure out animation
                    )
                }
            },
            onBackClicked = {
                navController.goBack()
            }
        )
    }

    private fun handleDetailsFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        animation: NavigationController.Animation? = null
    ) {
        fragmentManager.beginTransaction().apply {
            animation?.let { anim ->
                setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit)
            }
            replace(R.id.detailsContentContainer, fragment)
        }.commit()
    }
}