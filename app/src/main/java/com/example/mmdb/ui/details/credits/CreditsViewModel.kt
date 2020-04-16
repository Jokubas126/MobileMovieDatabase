package com.example.mmdb.ui.details.credits

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.example.mmdb.model.data.Credits
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.util.DEFAULT_ID_VALUE
import com.example.mmdb.util.isNetworkAvailable
import com.example.mmdb.util.managers.ProgressManager
import com.example.mmdb.util.networkUnavailableNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreditsViewModel(application: Application, arguments: Bundle?) :
    AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private var _credits = MutableLiveData<Credits>()

    var credits: LiveData<Credits> = _credits
    val loading: LiveData<Boolean> = progressManager.loading
    val error: LiveData<Boolean> = progressManager.error

    private lateinit var args: MovieDetailsArgs

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()
            arguments?.let {
                args = MovieDetailsArgs.fromBundle(it)
                if (args.movieLocalId == DEFAULT_ID_VALUE)
                    getCreditsRemote(args.movieRemoteId)
                else getCreditsLocal(args.movieLocalId)
            } ?: run { progressManager.error() }
        }
    }

    private fun getCreditsRemote(movieId: Int) {
        if (isNetworkAvailable(getApplication())) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = RemoteMovieRepository().getCredits(movieId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        withContext(Dispatchers.Main) {
                            _credits.value = Credits(0, it.castList, it.crewList)
                            progressManager.retrieved()
                        }
                    } ?: run { progressManager.error() }
                } else progressManager.error()
            }
        } else {
            progressManager.error()
            networkUnavailableNotification(getApplication())
        }
    }

    private fun getCreditsLocal(movieId: Int) {
        credits = RoomMovieRepository(getApplication()).getCreditsById(movieId)
        progressManager.retrieved()
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> {
                val action = CreditsFragmentDirections.actionMovieMedia()
                action.movieRemoteId = args.movieRemoteId
                action.movieLocalId = args.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
            R.id.overview_menu_item -> {
                val action = CreditsFragmentDirections.actionMovieOverview()
                action.movieRemoteId = args.movieRemoteId
                action.movieLocalId = args.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
        }
        return true
    }
}