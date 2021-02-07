package com.example.mmdb.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.MenuItem
import com.example.mmdb.R
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.navigation.NavigationActivity
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.AboutFragmentAction
import com.example.mmdb.ui.drawer.DrawerAction
import com.example.mmdb.ui.drawer.DrawerBehaviorInteractor
import com.example.mmdb.ui.movielists.rest.MovieListType
import com.example.mmdb.ui.movielists.rest.RemoteMovieGridFragmentAction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : NavigationActivity(R.layout.activity_main) {

    private var searchItem: MenuItem? = null
    private var confirmItem: MenuItem? = null

    override val navigationController: NavigationController by lazy {
        NavigationController(
            activity = this,
            drawerInteractor = DrawerBehaviorInteractor(
                drawerLayout = drawerLayout,
                contentView = rootContainer
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationController.goTo(
            action = RemoteMovieGridFragmentAction(MovieListType.Popular),
            animation = NavigationController.Animation.FadeIn
        )

        loadAppNavigation()
    }

    private fun loadAppNavigation() {
        menu_icon.setOnClickListener { navigationController.goTo(DrawerAction.Open) }
        navigationController.apply {
            navigation_view.setNavigationItemSelectedListener { menuItem ->
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
        prepareDrawerMenuItemCategoryStyle()
    }

    // to make drawer menu group titles styled
    private fun prepareDrawerMenuItemCategoryStyle() {
        val menu = navigation_view.menu
        for (i in 0 until menu.size()) {
            val tools = menu.getItem(i)
            val s = SpannableString(tools.title)
            s.setSpan(TextAppearanceSpan(this, R.style.ItemCategoryTextAppearance), 0, s.length, 0)
            tools.title = s
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        searchItem = menu.findItem(R.id.action_search)
        (searchItem?.actionView as SearchView?)?.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val action = NavGraphDirections.actionGlobalRemoteMovieGridFragment()
                    action.movieGridType = SEARCH_MOVIE_LIST
                    action.searchQuery = query
                    navController.popBackStack(navController.currentDestination!!.id, true)
                    navController.navigate(action)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        confirmItem = menu.findItem(R.id.action_confirm)
        confirmItem!!.isVisible = false
        return true
    }*/
}
