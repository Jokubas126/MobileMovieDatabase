package com.example.mmdb.navigation

import androidx.appcompat.app.AppCompatActivity

abstract class NavigationActivity() : AppCompatActivity() {

    abstract val navigationController: NavigationController

    override fun onBackPressed() {
        navigationController.goBack(
            alt = { super.onBackPressed() }
        )
    }
}