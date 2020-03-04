package com.example.moviesearcher.ui.details.cast

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Person
import com.example.moviesearcher.model.services.MovieDbApiService
import com.example.moviesearcher.model.services.responses.PersonListAsyncResponse
import com.example.moviesearcher.util.KEY_MOVIE_ID

class CastViewModel : ViewModel() {

    private val _cast = MutableLiveData<List<Person>>()
    private val _crew = MutableLiveData<List<Person>>()
    private val _loading = MutableLiveData<Boolean>()

    val cast: LiveData<List<Person>> = _cast
    val crew: LiveData<List<Person>> = _crew
    val loading: LiveData<Boolean> = _loading

    fun fetch(activity: Activity, args: Bundle?) {
        _loading.value = true
        Thread(Runnable {
            if (args != null) {
                MovieDbApiService().getPeople(args.getInt(KEY_MOVIE_ID),
                        PersonListAsyncResponse { castList, crewList ->
                            run {
                                activity.runOnUiThread {
                                    _cast.value = castList
                                    _crew.value = crewList
                                    _loading.setValue(false)
                                }
                            }
                        }
                )
            }
        }).start()
    }
}