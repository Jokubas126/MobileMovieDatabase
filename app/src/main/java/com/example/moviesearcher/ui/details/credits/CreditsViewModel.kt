package com.example.moviesearcher.ui.details.credits

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.moviesearcher.MovieDetailsArgs
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Person
import com.example.moviesearcher.model.repositories.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreditsViewModel : ViewModel() {

    private val _cast = MutableLiveData<List<Person>>()
    private val _crew = MutableLiveData<List<Person>>()
    private val _loading = MutableLiveData<Boolean>()

    val cast: LiveData<List<Person>> = _cast
    val crew: LiveData<List<Person>> = _crew
    val loading: LiveData<Boolean> = _loading

    private lateinit var safeArgs: MovieDetailsArgs

    fun fetch(arguments: Bundle?) {
        _loading.value = true
        arguments?.let {
            safeArgs = MovieDetailsArgs.fromBundle(it)
            if (safeArgs.movieLocalId == 0)
                getCreditsRemote(safeArgs.movieRemoteId)
            else getCreditsLocal(safeArgs.movieLocalId)
        }
    }

    private fun getCreditsRemote(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getCredits(movieId)
            withContext(Dispatchers.Main) {
                _cast.value = response.body()!!.castList
                _crew.value = response.body()!!.crewList
                _loading.value = false
            }
        }
    }

    private fun getCreditsLocal(movieId: Int) {
        TODO()
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> {
                val action = CreditsFragmentDirections.actionMovieMedia()
                action.movieRemoteId = safeArgs.movieRemoteId
                action.movieLocalId = safeArgs.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
            R.id.overview_menu_item -> {
                val action = CreditsFragmentDirections.actionMovieOverview()
                action.movieRemoteId = safeArgs.movieRemoteId
                action.movieLocalId = safeArgs.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
        }
        return true
    }
}