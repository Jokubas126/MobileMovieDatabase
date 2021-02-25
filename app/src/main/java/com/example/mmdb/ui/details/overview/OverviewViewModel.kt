package com.example.mmdb.ui.details.overview

import androidx.lifecycle.*
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverviewViewModel(
    private val action: InnerDetailsAction.OverviewAction,
    private val config: OverviewConfig
) : ViewModel() {

    private var _currentMovie = MutableLiveData<Movie?>()

    val currentMovie: LiveData<Movie?>
        get() = _currentMovie

    val progressManager = ProgressManager()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()

            when (val response = config.provideOverviewInfo.invoke(action.movieId)){
                is DataResponse.Success -> {
                    _currentMovie.postValue((response.value as OverviewInfo).movie)
                    progressManager.loaded()
                }
                is DataResponse.Error -> progressManager.error()
            }

        }
    }
}