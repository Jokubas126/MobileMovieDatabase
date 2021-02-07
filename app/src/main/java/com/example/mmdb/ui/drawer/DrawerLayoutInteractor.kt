package com.example.mmdb.ui.drawer

import android.content.Context
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import com.example.mmdb.R
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.AboutFragmentAction
import com.example.mmdb.ui.movielists.rest.MovieListType
import com.example.mmdb.ui.movielists.rest.RemoteMovieGridFragmentAction
import com.google.android.material.navigation.NavigationView

class DrawerLayoutInteractor(private val context: Context) {

    fun configureDrawerItems(
        navigationView: NavigationView,
        navigationController: NavigationController
    ) {
        prepareDrawerMenuItemCategoryStyle(navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                //R.id.menu_categories -> navController?.navigate(NavGraphDirections.actionGlobalCategoriesFragment())
                //R.id.menu_watchlist -> navController?.navigate(NavGraphDirections.actionGlobalWatchlistFragment())
                //R.id.menu_custom_lists -> navController?.navigate(NavGraphDirections.actionGlobalCustomListsFragment())
                R.id.menu_popular -> navigationController.goTo(
                    action = RemoteMovieGridFragmentAction(MovieListType.Popular),
                    animation = NavigationController.Animation.FadeIn
                )
                R.id.menu_top_rated -> navigationController.goTo(
                    action = RemoteMovieGridFragmentAction(MovieListType.TopRated),
                    animation = NavigationController.Animation.FadeIn
                )
                R.id.menu_now_playing -> navigationController.goTo(
                    action = RemoteMovieGridFragmentAction(MovieListType.NowPlaying),
                    animation = NavigationController.Animation.FadeIn
                )
                R.id.menu_upcoming -> navigationController.goTo(
                    action = RemoteMovieGridFragmentAction(MovieListType.Upcoming),
                    animation = NavigationController.Animation.FadeIn
                )
                R.id.menu_about -> navigationController.goTo(
                    action = AboutFragmentAction(),
                    animation = NavigationController.Animation.FadeIn
                )
            }
            navigationController.goTo(DrawerAction.Close)
            true
        }
    }

    // to make drawer menu group titles styled
    private fun prepareDrawerMenuItemCategoryStyle(navigationView: NavigationView) {
        val menu = navigationView.menu
        for (i in 0 until menu.size()) {
            val tools = menu.getItem(i)
            val s = SpannableString(tools.title)
            s.setSpan(
                TextAppearanceSpan(context, R.style.ItemCategoryTextAppearance),
                0,
                s.length,
                0
            )
            tools.title = s
        }
    }
}