package com.example.moviesearcher.ui.details.media

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Video
import com.example.moviesearcher.model.services.MovieDbApiService
import com.example.moviesearcher.model.services.responses.ImageListAsyncResponse
import com.example.moviesearcher.model.services.responses.ObjectAsyncResponse
import com.example.moviesearcher.util.KEY_MOVIE_ID

class MediaViewModel : ViewModel() {
    private val _trailer = MutableLiveData<Video>()
    private val _posterList = MutableLiveData<List<String>>()
    private val _backdropList = MutableLiveData<List<String>>()
    private val _loading = MutableLiveData<Boolean>()

    val trailer: LiveData<Video> = _trailer
    val posterList: LiveData<List<String>> = _posterList
    val backdropList: LiveData<List<String>> = _backdropList
    val loading: LiveData<Boolean> = _loading

    fun fetch(activity: Activity, args: Bundle?) {
        _loading.value = true
        if (args != null) {
            MovieDbApiService().getTrailer(args.getInt(KEY_MOVIE_ID), ObjectAsyncResponse {
                activity.runOnUiThread {
                    _trailer.value = it as Video
                    _loading.setValue(false)
                }
            })

            MovieDbApiService().getImages(args.getInt(KEY_MOVIE_ID),
                    ImageListAsyncResponse { backdropPathList, posterPathList ->
                        run {
                            activity.runOnUiThread {
                                _posterList.value = posterPathList
                                _backdropList.setValue(backdropPathList)
                            }
                        }
                    }
            )
        }
    }
}