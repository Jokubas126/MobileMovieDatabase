package com.example.mmdb.ui.details.media

import android.app.Application
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.example.mmdb.model.data.Images
import com.example.mmdb.model.data.Video
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.util.*
import com.example.mmdb.util.managers.ProgressManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaViewModel(application: Application, arguments: Bundle?) : AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private val _trailer = MutableLiveData<Video>()
    private val _images = MutableLiveData<Images>()

    val trailer: LiveData<Video> = _trailer
    var images: LiveData<Images>? = _images
    val loading: LiveData<Boolean> = progressManager.loading
    val error: LiveData<Boolean> = progressManager.error

    private lateinit var args: MovieDetailsArgs

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()
            arguments?.let {
                args = MovieDetailsArgs.fromBundle(it)
                if (args.movieLocalId == DEFAULT_ID_VALUE) {
                    if (isNetworkAvailable(getApplication())){
                        getImagesRemote(args.movieRemoteId)
                        getTrailer(args.movieRemoteId)
                    } else {
                        progressManager.error()
                        networkUnavailableNotification(getApplication())
                    }
                } else {
                    getImagesLocal(args.movieLocalId)
                }
            } ?: run { progressManager.error() }
        }
    }

    private fun getTrailer(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RemoteMovieRepository().getVideo(movieId)
            if (response.isSuccessful){
                withContext(Dispatchers.Main) {
                    _trailer.value = filterVideos(response.body()!!.videoList)
                    progressManager.retrieved()
                }
            } else
                progressManager.error()

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
        CoroutineScope(Dispatchers.IO).launch {
            val response = RemoteMovieRepository().getImages(movieId)
            if (response.isSuccessful) {
                response.body()?.let {
                    withContext(Dispatchers.Main) {
                        _images.value = Images(0, it.posterList, it.backdropList)
                        progressManager.retrieved()
                    }
                } ?: run { progressManager.error() }
            } else progressManager.error()
        }
    }

    private fun getImagesLocal(movieId: Int) {
        images = RoomMovieRepository(getApplication()).getImagesById(movieId)
        progressManager.retrieved()
    }

    fun onNavigationItemSelected(view: View, menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.overview_menu_item -> {
                val action = MediaFragmentDirections.actionMovieOverview()
                action.movieRemoteId = args.movieRemoteId
                action.movieLocalId = args.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
            R.id.cast_menu_item -> {
                val action = MediaFragmentDirections.actionMovieCast()
                action.movieRemoteId = args.movieRemoteId
                action.movieLocalId = args.movieLocalId
                Navigation.findNavController(view).navigate(action)
            }
        }
        return true
    }
}