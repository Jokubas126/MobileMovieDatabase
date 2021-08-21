package com.jokubas.mmdb.moviedetails

import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction

class DetailsFragmentConfig(
    val loadInitialView: (movieId: Int, isRemote: Boolean) -> Unit,
    val onBottomNavigationAction: (action: InnerDetailsAction) -> Unit,
    val onBackClicked: () -> Unit
)