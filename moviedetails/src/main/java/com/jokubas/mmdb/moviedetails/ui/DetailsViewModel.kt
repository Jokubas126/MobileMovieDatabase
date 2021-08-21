package com.jokubas.mmdb.moviedetails.ui

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import com.jokubas.mmdb.moviedetails.R
import com.jokubas.mmdb.moviedetails.actions.DetailsFragmentAction
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction

class DetailsViewModel(
    private val action: DetailsFragmentAction,
    private val config: DetailsFragmentConfig
) : ViewModel() {

    val onMenuItemSelected = { menuItem: MenuItem ->
        when (menuItem.itemId) {
            R.id.overview_menu_item ->
                config.onBottomNavigationAction.invoke(InnerDetailsAction.Overview(action.movieId, action.isRemote))
            R.id.media_menu_item ->
                config.onBottomNavigationAction.invoke(InnerDetailsAction.Media(action.movieId, action.isRemote))
            R.id.credits_menu_item ->
                config.onBottomNavigationAction.invoke(InnerDetailsAction.Credits(action.movieId, action.isRemote))
        }
    }

    val onBackClicked = config.onBackClicked
}