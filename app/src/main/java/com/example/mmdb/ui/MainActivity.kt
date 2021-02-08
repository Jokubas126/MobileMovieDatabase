package com.example.mmdb.ui

import android.os.Bundle
import android.view.MenuItem
import com.example.mmdb.R
import com.example.mmdb.navigation.NavigationActivity
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.configproviders.NavigationWrapperFragmentConfigProvider
import com.example.mmdb.ui.drawer.DrawerBehaviorInteractor
import com.example.mmdb.ui.drawer.DrawerLayoutInteractor
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction
import kotlinx.android.synthetic.main.activity_main.*

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

    private val drawerLayoutInteractor: DrawerLayoutInteractor by lazy {
        DrawerLayoutInteractor(context = baseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.rootContainer,
                NavigationWrapperFragment().apply {
                    arguments = NavigationWrapperFragmentArgs.create(
                        action = RemoteMovieGridFragmentAction(),
                        configProvider = NavigationWrapperFragmentConfigProvider::class.java
                    )
                })
            .commit()

        drawerLayoutInteractor.configureDrawerItems(navigationView, navigationController)
    }

    // TODO setup dynamic options menu in the toolbar

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
