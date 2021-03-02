package com.example.mmdb.ui.details

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import com.example.mmdb.R
import com.example.mmdb.navigation.actions.DetailsFragmentAction
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.example.mmdb.ui.ToolbarViewModel

class DetailsViewModel(
    private val action: DetailsFragmentAction,
    private val config: DetailsFragmentConfig,
    val toolbarViewModel: ToolbarViewModel
) : ViewModel() {

    val onMenuItemSelected = { menuItem: MenuItem ->
        when (menuItem.itemId) {
            R.id.overview_menu_item ->
                config.onBottomNavigationAction.invoke(InnerDetailsAction.Overview(action.idWrapper))
            R.id.media_menu_item ->
                config.onBottomNavigationAction.invoke(InnerDetailsAction.Media(action.idWrapper))
            R.id.credits_menu_item ->
                config.onBottomNavigationAction.invoke(InnerDetailsAction.Credits(action.idWrapper))
        }
    }

    init {
        config.loadInitialView.invoke(action.idWrapper)
    }
}