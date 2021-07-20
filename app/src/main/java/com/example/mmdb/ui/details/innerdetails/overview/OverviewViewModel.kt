package com.example.mmdb.ui.details.innerdetails.overview

import androidx.lifecycle.*
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
        viewModelScope.launch(Dispatchers.IO) {
            config.provideOverviewInfo.invoke(action.movieId, action.isRemote).collect { response ->
                when (response) {
                    is DataResponse.Success<Movie> -> {
                        response.body()?.let {
                            _currentMovie.postValue(it)
                            progressManager.success()
                        } ?: progressManager.error()
                    }
                    is DataResponse.Error -> progressManager.error()
                    is DataResponse.Loading -> progressManager.loading()
                    else -> progressManager.error()
                }

            }
        }
    }
}