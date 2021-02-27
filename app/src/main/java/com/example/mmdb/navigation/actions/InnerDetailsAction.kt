package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import com.example.mmdb.ui.details.IdWrapper
import kotlinx.android.parcel.Parcelize

sealed class InnerDetailsAction: Action {

    @Parcelize
    class Overview(
        val movieIdWrapper: IdWrapper
    ): InnerDetailsAction()

    @Parcelize
    class Media(
        val movieIdWrapper: IdWrapper
    ): InnerDetailsAction()

    @Parcelize
    class Credits(
        val movieIdWrapper: IdWrapper
    ): InnerDetailsAction()
}