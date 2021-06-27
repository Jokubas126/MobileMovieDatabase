package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import kotlinx.android.parcel.Parcelize

sealed class InnerDetailsAction: Action {

    @Parcelize
    class Overview(
        val movieId: Int,
        val isRemote: Boolean
    ): InnerDetailsAction()

    @Parcelize
    class Media(
        val movieId: Int,
        val isRemote: Boolean
    ): InnerDetailsAction()

    @Parcelize
    class Credits(
        val movieId: Int,
        val isRemote: Boolean
    ): InnerDetailsAction()
}