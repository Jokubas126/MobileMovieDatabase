package com.jokubas.mmdb.util.extensions

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Window.adjustStatusBar(@ColorRes colorRes: Int) {
    statusBarColor = ContextCompat.getColor(context, colorRes)
    setLightStatusBar(true)
}

fun Window.setLightStatusBar(light: Boolean) {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            val appearance = if (light)
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            else WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS.inv()
            decorView.windowInsetsController?.setSystemBarsAppearance(appearance, appearance)
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            var flags = decorView.systemUiVisibility
            flags = if (light) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = flags
        }
    }
}