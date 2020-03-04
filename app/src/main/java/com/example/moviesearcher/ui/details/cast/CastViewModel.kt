package com.example.moviesearcher.ui.details.cast

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Person
import com.example.moviesearcher.model.services.MovieDbApiService
import com.example.moviesearcher.util.BundleUtil

class CastViewModel: ViewModel() {

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
                MovieDbApiService().getPeople(args.getInt(BundleUtil.KEY_MOVIE_ID)
                ) { castList: List<Person>, crewList: List<Person> ->
                    activity.runOnUiThread {
                        _cast.value = castList
                        _crew.value = crewList
                        _loading.setValue(false)
                    }
                }
            }
        }).start()
    }
}