package com.example.mmdb.ui

import android.os.Bundle
import android.view.MenuItem
import com.example.mmdb.R
import com.example.mmdb.config.AppConfig
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.navigation.NavigationActivity
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.configproviders.NavigationWrapperFragmentConfigProvider
import com.example.mmdb.ui.drawer.DrawerBehaviorInteractor
import com.example.mmdb.ui.drawer.DrawerLayoutInteractor
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.jokubas.mmdb.util.extensions.adjustStatusBar
import com.jokubas.mmdb.util.extensions.setLockMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : NavigationActivity(R.layout.activity_main) {

    private var searchItem: MenuItem? = null
    private var confirmItem: MenuItem? = null

    private val appConfig: AppConfig by lazy {
        requireAppConfig()
    }

    override val navigationController: NavigationController by lazy {
        NavigationController(
            activity = this,
            drawerInteractor = DrawerBehaviorInteractor(
                drawerLayout = drawerLayout,
                drawerConfig = appConfig.drawerConfig,
                contentView = rootContainer
            ),
            drawerConfig = appConfig.drawerConfig
        )
    }

    private val drawerLayoutInteractor: DrawerLayoutInteractor by lazy {
        DrawerLayoutInteractor(context = baseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeDrawerLock()

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.rootContainer,
                NavigationWrapperFragment(attachToNavigation = false).apply {
                    arguments = NavigationWrapperFragmentArgs.create(
                        action = MovieGridFragmentAction(),
                        configProvider = NavigationWrapperFragmentConfigProvider::class.java
                    )
                    appConfig.toolbarConfig.setDrawerFragment()
                })
            .commit()

        drawerLayoutInteractor.configureDrawerItems(navigationView, navigationController)
        window.adjustStatusBar(R.color.white)
    }

    private fun observeDrawerLock(){
        appConfig.drawerConfig.isDrawerEnabledLiveData().observe(this, { isEnabled ->
            drawerLayout.setLockMode(isEnabled)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        appConfig.drawerConfig.isDrawerEnabledLiveData().removeObservers(this)
    }
    // TODO setup dynamic options menu in the toolbar through its viewModel

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
