package com.example.mmdb.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.ui.NavigationUI
import com.example.mmdb.NavGraphDirections
import com.example.mmdb.R
import com.example.mmdb.util.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView

    private var searchItem: MenuItem? = null
    private var confirmItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        loadAppNavigation()
        prepareDrawerMenuItemCategoryStyle()
    }

    private fun loadAppNavigation() {
        navigationView = navigation_view
        drawerLayout = drawer_layout

        navigationView.setNavigationItemSelectedListener(this)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, _: Bundle? ->
            run {
                if (destination != controller.graph.findNode(R.id.categoriesFragment)) {
                    setSupportActionBar(toolbar)
                    searchItem?.isVisible = true
                    confirmItem?.isVisible = false
                    toolbar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    // to make drawer menu group titles styled
    private fun prepareDrawerMenuItemCategoryStyle() {
        val menu = navigationView.menu
        for (i in 0 until menu.size()) {
            val tools = menu.getItem(i)
            val s = SpannableString(tools.title)
            s.setSpan(TextAppearanceSpan(this, R.style.ItemCategoryTextAppearance), 0, s.length, 0)
            tools.title = s
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem!!.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val action = NavGraphDirections.actionGlobalRemoteMovieGridFragment()
                action.movieGridType = SEARCH_MOVIE_GRID
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
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_categories -> navController.navigate(NavGraphDirections.actionGlobalCategoriesFragment())
            R.id.menu_watchlist -> navController.navigate(NavGraphDirections.actionGlobalWatchlistFragment())
            R.id.menu_custom_lists -> navController.navigate(NavGraphDirections.actionGlobalCustomListsFragment())

            R.id.menu_popular -> {
                val action
                        = NavGraphDirections.actionGlobalRemoteMovieGridFragment()
                action.keyCategory = KEY_POPULAR
                navController.navigate(action)
            }
            R.id.menu_top_rated -> {
                val action
                        = NavGraphDirections.actionGlobalRemoteMovieGridFragment()
                action.keyCategory = KEY_TOP_RATED
                navController.navigate(action)
            }
            R.id.menu_now_playing -> {
                val action
                        = NavGraphDirections.actionGlobalRemoteMovieGridFragment()
                action.keyCategory = KEY_NOW_PLAYING
                navController.navigate(action)
            }
            R.id.menu_upcoming -> {
                val action
                        = NavGraphDirections.actionGlobalRemoteMovieGridFragment()
                action.keyCategory = KEY_UPCOMING
                navController.navigate(action)
            }
            R.id.menu_about -> navController.navigate(NavGraphDirections.actionGlobalAboutFragment())
        }
        drawerLayout.closeDrawers()
        return true
    }
}
