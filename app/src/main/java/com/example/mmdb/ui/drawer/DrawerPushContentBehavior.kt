package com.example.mmdb.ui.drawer

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mmdb.config.DrawerConfig

class DrawerPushContentBehavior(
    private val contentView: View
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
    }
}