package com.example.mmdb.navigation

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class NavigationActivity(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {

    abstract val navigationController: NavigationController

    override fun onBackPressed() {
        if (navigationController.isOnForeground()) {
            super.onBackPressed()
        } else {
            navigationController.goBack()
        }
    }
}