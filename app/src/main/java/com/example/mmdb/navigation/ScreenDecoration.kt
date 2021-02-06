package com.example.mmdb.navigation

/**
 * This class determines how the wrapper fragment is going to look
 * - Toolbar: adds a toolbar with a static title
 * - None: fragment is in fullscreen, without any toolbar
 */
sealed class ScreenDecoration {

    open class Toolbar(
        protected var title: String
    ) : ScreenDecoration() {

        /*constructor(
            @StringRes titleRes: Int
        ) : this(
            title = titleRes.toTitle()
        )*/
    }

    object None : ScreenDecoration()
}