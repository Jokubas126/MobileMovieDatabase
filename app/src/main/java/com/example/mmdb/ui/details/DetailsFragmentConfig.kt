package com.example.mmdb.ui.details

import com.example.mmdb.navigation.actions.InnerDetailsAction

class DetailsFragmentConfig(
    val loadInitialView: (idWrapper: IdWrapper) -> Unit,
    val onBottomNavigationAction: (action: InnerDetailsAction) -> Unit
)