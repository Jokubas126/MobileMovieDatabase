package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import com.example.mmdb.ui.details.IdWrapper
import kotlinx.android.parcel.Parcelize

sealed class InnerDetailsAction: Action {

    @Parcelize
    class OverviewAction(
        val movieIdWrapper: IdWrapper
    ): InnerDetailsAction()

    @Parcelize
    class MediaAction(
        val movieIdWrapper: IdWrapper
    ): InnerDetailsAction()

    @Parcelize
    class CreditsAction(
        val movieIdWrapper: IdWrapper
    ): InnerDetailsAction()
}