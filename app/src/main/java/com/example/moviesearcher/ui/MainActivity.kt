package com.example.moviesearcher.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.moviesearcher.NavGraphDirections
import com.example.moviesearcher.R
import com.example.moviesearcher.util.*
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

        Glide.with(this).load(R.drawable.background).into(background_image_view)
        setSupportActionBar(toolbar)

        navigationView = navigation_view
        drawerLayout = drawer_layout

        navigationView.setNavigationItemSelectedListener(this)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, _: Bundle? ->
            run {
                if (destination != controller.graph.findNode(R.id.categoriesFragment)){
                    setSupportActionBar(toolbar)
                    searchItem?.isVisible = true
                    confirmItem?.isVisible = false
                    toolbar.visibility = View.VISIBLE
                }
            }
        }
        prepareDrawerMenuItemCategoryStyle()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

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
                val bundle = bundleOf(KEY_SEARCH_QUERY to query)
                navController.popBackStack(navController.currentDestination!!.id, true)
                navController.navigate(R.id.actionGlobalSearchGridFragment, bundle)
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
        val bundle = Bundle()
        when (menuItem.itemId) {
            R.id.menu_categories -> navController.navigate(R.id.discoverMovies)
            R.id.menu_custom_lists -> navController.navigate(R.id.customListsFragment)

            R.id.menu_popular -> {
                bundle.putString(KEY_MOVIE_LIST_TYPE, KEY_POPULAR)
                navController.navigate(R.id.typeGridFragment, bundle)
            }
            R.id.menu_top_rated -> {
                bundle.putString(KEY_MOVIE_LIST_TYPE, KEY_TOP_RATED)
                navController.navigate(R.id.typeGridFragment, bundle)
            }
            R.id.menu_now_playing -> {
                bundle.putString(KEY_MOVIE_LIST_TYPE, KEY_NOW_PLAYING)
                navController.navigate(R.id.typeGridFragment, bundle)
            }
            R.id.menu_upcoming -> {
                bundle.putString(KEY_MOVIE_LIST_TYPE, KEY_UPCOMING)
                navController.navigate(R.id.typeGridFragment, bundle)
            }

            R.id.menu_about -> navController.navigate(R.id.aboutFragment)
        }
        drawerLayout.closeDrawers()
        return true
    }
}
