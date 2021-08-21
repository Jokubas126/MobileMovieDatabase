package com.jokubas.mmdb.moviedetails.actions

import com.jokubas.mmdb.util.navigationtools.Action
import kotlinx.parcelize.Parcelize

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