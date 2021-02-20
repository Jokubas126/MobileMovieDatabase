package com.example.mmdb.navigation

sealed class ScreenDecoration {

    object WithDrawer : ScreenDecoration()
    object NoDrawer : ScreenDecoration()
}