package com.example.mmdb.ui.details.media

import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.config.AppConfig
import com.example.mmdb.managers.ProgressManager
import com.jokubas.mmdb.model.data.entities.Image
import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.Video
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.jokubas.mmdb.util.*
import com.jokubas.mmdb.util.constants.KEY_TRAILER_TYPE
import com.jokubas.mmdb.util.constants.KEY_YOUTUBE_SITE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MediaViewModel(
    appConfig: AppConfig,
    private val movieLocalId: Int,
    private val movieRemoteId: Int
) : ViewModel() {

    val progressManager = ProgressManager()

    private val _trailer = MutableLiveData<Video?>()
    private val _images = MutableLiveData<Images?>()

    val trailer: LiveData<Video?>
        get() = _trailer
    val images: LiveData<Images?>
        get() = _images

    val imageBinding: ItemBinding<Image> = ItemBinding.of(BR.image, R.layout.item_image)

    private val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository
    //private val roomMovieRepository by lazy { RoomMovieRepository(application) }

    init {
        progressManager.loading()
        CoroutineScope(Dispatchers.IO).launch {
            when (movieLocalId) {
                DEFAULT_ID_VALUE -> {
                    if (appConfig.networkCheckConfig.isNetworkAvailable()) {
                        getImagesRemote()
                        getTrailer(movieRemoteId)
                        progressManager.loaded()
                    } else {
                        appConfig.networkCheckConfig.networkUnavailableNotification()
                        progressManager.error()
                    }
                }
                else -> {
                    getImagesLocal()
                    progressManager.loaded()
                }
            }
        }
    }

    private suspend fun getTrailer(movieId: Int) {
        _trailer.postValue(filterVideos(remoteMovieRepository.getVideo(movieId).videoList))
    }

    private fun filterVideos(videoList: List<Video>): Video? {
        videoList.forEach { video ->
            if (video.siteType == KEY_YOUTUBE_SITE && video.videoType == KEY_TRAILER_TYPE)
                return video
        }
        return null
    }

    private suspend fun getImagesRemote() {
        _images.postValue(remoteMovieRepository.getImages(movieRemoteId))
    }

    private suspend fun getImagesLocal() {
        //_images.postValue(roomMovieRepository.getImagesById(movieLocalId))
    }
}