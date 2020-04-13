package com.example.moviesearcher.ui.details.credits

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.MovieDetailsArgs
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Credits
import com.example.moviesearcher.model.remote.repositories.RemoteMovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreditsViewModel(application: Application, arguments: Bundle?) : AndroidViewModel(application) {

    private var _credits = MutableLiveData<Credits>()
    private val _loading = MutableLiveData<Boolean>()

    var credits: LiveData<Credits> = _credits
    val loading: LiveData<Boolean> = _loading

    private lateinit var args: MovieDetailsArgs

    init {
        _loading.value = true
        arguments?.let {
            args = MovieDetailsArgs.fromBundle(it)
            if (args.movieLocalId == 0)
                getCreditsRemote(args.movieRemoteId)
            else getCreditsLocal(args.movieLocalId)
        }
    }

    private fun getCreditsRemote(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RemoteMovieRepository()
                .getCredits(movieId)
            withContext(Dispatchers.Main) {
                _credits.value = Credits(0, response.body()!!.castList, response.body()!!.crewList)
                _loading.value = false
            }
        }
    }

    private fun getCreditsLocal(movieId: Int) {
        credits = com.example.moviesearcher.model.room.repositories.RoomMovieRepository(
            getApplication()
        ).getCreditsById(movieId)
        _loading.value = false
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