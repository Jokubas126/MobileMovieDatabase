package com.example.mmdb.ui.drawer

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mmdb.config.DrawerConfig

class DrawerPushContentBehavior(
    private val drawerLayout: DrawerLayout,
    private val contentView: View,
    private val drawerConfig: DrawerConfig
) : DrawerLayout.DrawerListener {

    private var drawerOpened = false

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        val translationX = drawerView.width * slideOffset
        contentView.translationX = translationX
    }

    override fun onDrawerOpened(drawerView: View) {
        drawerOpened = true
    }

    override fun onDrawerClosed(drawerView: View) {
        drawerOpened = false
    }

    override fun onDrawerStateChanged(newState: Int) {
        when {
            drawerConfig.isDrawerEnabled.get() -> drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            !drawerConfig.isDrawerEnabled.get() && !drawerOpened -> drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            !drawerConfig.isDrawerEnabled.get() && drawerOpened -> drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
        }
    }
}