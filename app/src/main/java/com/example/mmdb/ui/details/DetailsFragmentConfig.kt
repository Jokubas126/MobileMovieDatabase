package com.example.mmdb.ui.details

import com.example.mmdb.navigation.actions.InnerDetailsAction

class DetailsFragmentConfig(
    val loadInitialView: (movieId: Int, isRemote: Boolean) -> Unit,
    val onBottomNavigationAction: (action: InnerDetailsAction) -> Unit
)