package com.example.mmdb.ui.details.credits

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.example.mmdb.model.data.Credits
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.util.DEFAULT_ID_VALUE
import com.example.mmdb.util.isNetworkAvailable
import com.example.mmdb.util.networkUnavailableNotification

class CreditsViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val args = arguments?.let { MovieDetailsArgs.fromBundle(it) }

    private val _credits = args?.let {
        if (it.movieLocalId == DEFAULT_ID_VALUE) {
            if (isNetworkAvailable(getApplication())) {
                RemoteMovieRepository().getCreditsFlow(args.movieRemoteId)
                    .asLiveData(viewModelScope.coroutineContext)
            } else {
                networkUnavailableNotification(getApplication())
                null
            }
        } else
            RoomMovieRepository(getApplication()).getCreditsFlowById(args.movieLocalId)
                .asLiveData(viewModelScope.coroutineContext)
    }

    val credits: LiveData<Credits>?
        get() = _credits

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        return args?.let {
            when (menuItem.itemId) {
                R.id.media_menu_item -> {
                    val action = CreditsFragmentDirections.actionMovieMedia()
                    action.movieRemoteId = it.movieRemoteId
                    action.movieLocalId = it.movieLocalId
                    Navigation.findNavController(view).navigate(action)
                }
                R.id.overview_menu_item -> {
                    val action = CreditsFragmentDirections.actionMovieOverview()
                    action.movieRemoteId = it.movieRemoteId
                    action.movieLocalId = it.movieLocalId
                    Navigation.findNavController(view).navigate(action)
                }
            }
            true
        } ?: run { false }
    }
}