package com.example.mmdb.navigation

import android.os.Parcelable

interface NavigationDirections {

    fun goTo(
        action: Parcelable,
        animation: NavigationController.Animation? = NavigationController.Animation.FromRight,
        shouldAddWrapper: Boolean = true
    )

    fun goBack(
        toRoot: Boolean = false
    )

    fun isOnForeground(): Boolean
}