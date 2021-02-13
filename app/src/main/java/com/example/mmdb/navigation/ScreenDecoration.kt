package com.example.mmdb.navigation

/**
 * This class determines how the wrapper fragment is going to look
 * - Toolbar: adds a toolbar with a static title
 * - None: fragment is in fullscreen, without any toolbar
 */
sealed class ScreenDecoration {

    object Wrapped : ScreenDecoration()
    object Full : ScreenDecoration()
}