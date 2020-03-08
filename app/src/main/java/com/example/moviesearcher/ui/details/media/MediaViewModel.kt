package com.example.moviesearcher.ui.details.media

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearcher.model.data.Image
import com.example.moviesearcher.model.data.Video
import com.example.moviesearcher.model.repositories.MovieRepository
import com.example.moviesearcher.util.KEY_MOVIE_ID
import com.example.moviesearcher.util.KEY_TRAILER_TYPE
import com.example.moviesearcher.util.KEY_YOUTUBE_SITE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaViewModel : ViewModel() {
    private val _trailer = MutableLiveData<Video>()
    private val _posterList = MutableLiveData<List<Image>>()
    private val _backdropList = MutableLiveData<List<Image>>()
    private val _loading = MutableLiveData<Boolean>()

    val trailer: LiveData<Video> = _trailer
    val posterList: LiveData<List<Image>> = _posterList
    val backdropList: LiveData<List<Image>> = _backdropList
    val loading: LiveData<Boolean> = _loading

    fun fetch(args: Bundle?) {
        _loading.value = true
        if (args != null) {
            val movieId = args.getInt(KEY_MOVIE_ID)
            getImages(movieId)
            getTrailer(movieId)
        }
    }

    private fun getTrailer(movieId: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getVideo(movieId)
            withContext(Dispatchers.Main){
                _trailer.value = filterVideos(response.body()!!.videoList)
                _loading.value = false
            }
        }
    }

    private fun filterVideos(videoList: List<Video>): Video? {
        for (video in videoList){
            if (video.siteType == KEY_YOUTUBE_SITE && video.videoType == KEY_TRAILER_TYPE)
                return video
        }
        return null
    }

    private fun getImages(movieId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getImages(movieId)
            withContext(Dispatchers.Main) {
                _posterList.value = response.body()!!.posterList
                _backdropList.value = response.body()!!.backdropList
                _loading.value = false
            }
        }
    }
}