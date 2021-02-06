package com.example.mmdb.ui.drawer

import android.graphics.Color
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

class DrawerBehaviorInteractor(
    private val drawerLayout: DrawerLayout,
    contentView: View,
    style: DrawerStyle = DrawerStyle()
) : DrawerBehaviorInterface {

    init {
        setupDrawer(contentView, style)
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun setupDrawer(
        contentView: View,
        style: DrawerStyle
    ) {
        val drawerListener = when (style.behavior) {
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