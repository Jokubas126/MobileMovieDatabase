package com.example.mmdb.ui.details.innerdetails.overview

import androidx.lifecycle.*
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val action: InnerDetailsAction.Overview,
    private val config: OverviewConfig
) : ViewModel() {

    private var _currentMovie = MutableLiveData<Movie?>()

    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    val progressManager = ProgressManager()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()

            when (val response = config.provideOverviewInfo.invoke(action.movieIdWrapper)){
                is DataResponse.Success<*> -> {
                    (response.value as? OverviewInfo)?.let {
                        _currentMovie.postValue(it.movie)
                        progressManager.loaded()
                    } ?: progressManager.error()
                }
                is DataResponse.Error -> progressManager.error()
            }
        }
    }
}