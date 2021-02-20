package com.example.mmdb.ui.drawer

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mmdb.config.DrawerConfig

class DrawerPushContentBehavior(
    private val drawerLayout: DrawerLayout,
    private val contentView: View,
    private val drawerConfig: DrawerConfig
) : DrawerLayout.DrawerListener {

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        val translationX = drawerView.width * slideOffset
        contentView.translationX = translationX
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerClosed(drawerView: View) {
    }

    override fun onDrawerStateChanged(newState: Int) {
        setLock()
    }

    private fun setLock(){
        when (drawerConfig.isDrawerEnabled.get()) {
            true -> {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            else -> {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }
}