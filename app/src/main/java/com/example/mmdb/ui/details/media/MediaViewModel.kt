package com.example.mmdb.ui.details.media

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.jokubas.mmdb.model.data.dataclasses.Images
import com.jokubas.mmdb.model.data.dataclasses.Video
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.util.KEY_TRAILER_TYPE
import com.jokubas.mmdb.util.KEY_YOUTUBE_SITE
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaViewModel(application: Application, arguments: Bundle?) : AndroidViewModel(application) {

    private val _trailer = MutableLiveData<Video>()
    private val _images = MutableLiveData<Images>()

    val trailer: LiveData<Video>
        get() = _trailer
    val images: LiveData<Images>
        get() = _images

    private var args = arguments?.let { MovieDetailsArgs.fromBundle(it) }

    init {
        CoroutineScope(Dispatchers.Default + viewModelScope.coroutineContext).launch {
            if (args?.movieLocalId == com.jokubas.mmdb.util.DEFAULT_ID_VALUE) {
                if (isNetworkAvailable(getApplication())) {
                    args?.let {
                        getImagesRemote(it.movieRemoteId)
                        getTrailer(it.movieRemoteId)
                    }
                } else
                    networkUnavailableNotification(
                        getApplication()
                    )
            } else
                args?.let { getImagesLocal(it.movieLocalId) }
        }
    }

    private fun getTrailer(movieId: Int) {
        viewModelScope.launch {
            val videoResults = RemoteMovieRepository()
                .getVideo(movieId)
            _trailer.value = filterVideos(videoResults.videoList)
        }
    }

    private fun filterVideos(videoList: List<Video>): Video? {
        for (video in videoList) {
            if (video.siteType == KEY_YOUTUBE_SITE && video.videoType == KEY_TRAILER_TYPE)
                return video
        }
        return null
    }

    private fun getImagesRemote(movieId: Int) {
        viewModelScope.launch {
            _images.value = RemoteMovieRepository()
                .getImages(movieId)
        }
    }

    private fun getImagesLocal(movieId: Int) {
        viewModelScope.launch {
            _images.value = com.jokubas.mmdb.model.room.repositories.RoomMovieRepository(
                getApplication()
            ).getImagesById(movieId)
        }
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.overview_menu_item -> {
                args?.let {
                    val action = MediaFragmentDirections.actionMovieOverview()
                    action.movieRemoteId = it.movieRemoteId
                    action.movieLocalId = it.movieLocalId
                    Navigation.findNavController(view).navigate(action)
                }
            }
            R.id.cast_menu_item -> {
                args?.let {
                    val action = MediaFragmentDirections.actionMovieCredits()
                    action.movieRemoteId = it.movieRemoteId
                    action.movieLocalId = it.movieLocalId
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
        return true
    }
}