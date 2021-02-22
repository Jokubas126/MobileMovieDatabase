package com.example.mmdb.navigation

import android.os.Parcelable

interface NavigationDirections {

    fun goTo(
        action: Parcelable,
        animation: NavigationController.Animation = NavigationController.Animation.FadeIn,
        useWrapper: Boolean = true
    )

    fun goBack(alt: (() -> Unit)? = null)

    fun putInWrapper(
        action: Parcelable,
        animation: NavigationController.Animation? = null
    )
}