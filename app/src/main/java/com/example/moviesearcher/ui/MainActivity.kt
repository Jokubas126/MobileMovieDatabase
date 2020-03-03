package com.example.moviesearcher.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.moviesearcher.R
import com.example.moviesearcher.util.BundleUtil
import com.example.moviesearcher.util.ConverterUtil
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView

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

        navController.addOnDestinationChangedListener { _: NavController?,
                                                        destination: NavDestination,
                                                        arguments: Bundle? ->
            if (destination.id != R.id.moviesList && supportActionBar != null)
                supportActionBar!!.title = destination.label
            else {
                if (arguments != null && supportActionBar != null)
                    supportActionBar!!.title = ConverterUtil.bundleToToolbarTitle(arguments)
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
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val bundle = Bundle()
                bundle.putString(BundleUtil.KEY_SEARCH_QUERY, query)
                navController.navigate(R.id.moviesList, bundle)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    val bundle = Bundle()
                    bundle.putString(BundleUtil.KEY_SEARCH_QUERY, null)
                    navController.navigate(R.id.moviesList, bundle)
                }
                return false
            }
        })
        return true
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val bundle = Bundle()
        when (menuItem.itemId) {
            R.id.menu_categories -> {
                navController.navigate(R.id.categoriesFragment)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.menu_popular -> {
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_POPULAR)
                navController.navigate(R.id.moviesList, bundle)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.menu_top_rated -> {
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_TOP_RATED)
                navController.navigate(R.id.moviesList, bundle)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.menu_now_playing -> {
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_NOW_PLAYING)
                navController.navigate(R.id.moviesList, bundle)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.menu_upcoming -> {
                bundle.putString(BundleUtil.KEY_MOVIE_LIST_TYPE, BundleUtil.KEY_UPCOMING)
                navController.navigate(R.id.moviesList, bundle)
                drawerLayout.closeDrawers()
                return true
            }
            R.id.menu_about -> {
                navController.navigate(R.id.aboutFragment)
                drawerLayout.closeDrawers()
                return true
            }
        }
        return false
    }
}
