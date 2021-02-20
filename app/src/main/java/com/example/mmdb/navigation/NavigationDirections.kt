package com.example.mmdb.navigation

import android.os.Parcelable

interface NavigationDirections {

    fun goTo(
        action: Parcelable,
        animation: NavigationController.Animation? = NavigationController.Animation.FromRight,
        shouldAddWrapper: Boolean = true
    )

    fun goBack()

    fun putInWrapper(
        action: Parcelable
    )

    fun isOnForeground(): Boolean
}