package com.example.mmdb.ui.drawer

import android.graphics.Color
import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.Observable
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mmdb.config.DrawerConfig

class DrawerBehaviorInteractor(
    private val drawerLayout: DrawerLayout,
    contentView: View,
    private val drawerConfig: DrawerConfig,
    style: DrawerStyle = DrawerStyle()
) : DrawerBehaviorInterface {

    init {
        setupDrawer(contentView, style)
    }

    override fun openDrawer() {
        if (drawerConfig.isDrawerEnabled())
            drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupDrawer(
        contentView: View,
        style: DrawerStyle
    ) {
        val drawerListener: DrawerLayout.DrawerListener = when (style.behavior) {
            DrawerBehavior.PushContent -> DrawerPushContentBehavior(contentView)
        }

        drawerLayout.apply {
            addDrawerListener(drawerListener)
            setScrimColor(style.overlayColor)
        }
    }
}

data class DrawerStyle(
    val overlayColor: Int = Color.TRANSPARENT,
    val behavior: DrawerBehavior = DrawerBehavior.PushContent
)

enum class DrawerBehavior {
    PushContent
}