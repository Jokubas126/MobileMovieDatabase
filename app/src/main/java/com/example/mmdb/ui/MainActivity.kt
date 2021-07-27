package com.example.mmdb.ui

import android.os.Bundle
import android.view.MenuItem
import com.example.mmdb.R
import com.example.mmdb.config.AppConfig
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.databinding.ActivityMainBinding
import com.example.mmdb.navigation.NavigationActivity
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.configproviders.NavigationWrapperFragmentConfigProvider
import com.example.mmdb.ui.drawer.DrawerBehaviorInteractor
import com.example.mmdb.ui.drawer.DrawerLayoutInteractor
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.jokubas.mmdb.util.extensions.adjustStatusBar
import com.jokubas.mmdb.util.extensions.setLockMode

class MainActivity : NavigationActivity() {

    private var searchItem: MenuItem? = null
    private var confirmItem: MenuItem? = null

    private val appConfig: AppConfig by lazy {
        requireAppConfig()
    }

    private lateinit var binding: ActivityMainBinding

    override val navigationController: NavigationController by lazy {
        NavigationController(
            activity = this,
            drawerInteractor = DrawerBehaviorInteractor(
                drawerLayout = binding.drawerLayout,
                drawerConfig = appConfig.drawerConfig,
                contentView = binding.rootContainer
            ),
            drawerConfig = appConfig.drawerConfig
        )
    }

    private val drawerLayoutInteractor: DrawerLayoutInteractor by lazy {
        DrawerLayoutInteractor(context = baseContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        drawerLayoutInteractor.configureDrawerItems(binding.navigationView, navigationController)
        window.adjustStatusBar(R.color.white)
    }

    private fun observeDrawerLock(){
        appConfig.drawerConfig.isDrawerEnabledLiveData().observe(this, { isEnabled ->
            binding.drawerLayout.setLockMode(isEnabled)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        appConfig.drawerConfig.isDrawerEnabledLiveData().removeObservers(this)
    }
}
