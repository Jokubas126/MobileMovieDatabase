package com.example.mmdb.navigation.actions

import com.example.mmdb.navigation.Action
import com.example.mmdb.ui.details.DetailsMovieId
import kotlinx.android.parcel.Parcelize

sealed class InnerDetailsAction: Action {

    @Parcelize
    class OverviewAction(
        val movieId: DetailsMovieId
    ): InnerDetailsAction()

    @Parcelize
    class MediaAction(
        val movieId: DetailsMovieId
    ): InnerDetailsAction()

    @Parcelize
    class CreditsAction(
        val movieId: DetailsMovieId
    ): InnerDetailsAction()
}