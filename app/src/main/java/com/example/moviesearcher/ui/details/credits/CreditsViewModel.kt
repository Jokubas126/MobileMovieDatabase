package com.example.moviesearcher.ui.details.credits

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Person
import com.example.moviesearcher.model.repositories.MovieRepository
import com.example.moviesearcher.util.KEY_MOVIE_ID
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

    fun fetch(args: Bundle?) {
        _loading.value = true
        if (args != null)
            getCredits(args.getInt(KEY_MOVIE_ID))
    }

    private fun getCredits(movieId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getCredits(movieId)
            withContext(Dispatchers.Main){
                _cast.value = response.body()!!.castList
                _crew.value = response.body()!!.crewList
                _loading.value = false
            }
        }
    }
}