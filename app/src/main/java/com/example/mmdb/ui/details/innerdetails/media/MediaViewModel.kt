package com.example.mmdb.ui.details.innerdetails.media

import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.jokubas.mmdb.model.data.entities.Image
import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.Video
import com.jokubas.mmdb.model.data.entities.filterMainTrailer
import com.jokubas.mmdb.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MediaViewModel(
    action: InnerDetailsAction.Media,
    config: MediaConfig
) : ViewModel() {

    val progressManager = ProgressManager()

    private val _trailer = MutableLiveData<Video?>()
    private val _images = MutableLiveData<Images?>()

    val trailer: LiveData<Video?>
        get() = _trailer
    val images: LiveData<Images?>
        get() = _images

    val imageBinding: ItemBinding<Image> = ItemBinding.of(BR.image, R.layout.item_image)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            config.provideImages.invoke(action.movieId, action.isRemote).collect { images ->
                if (images is DataResponse.Success<*>) {
                    images.body()?.let {
                        _images.postValue(it)
                    }
                }
            }
            config.provideTrailer.invoke(action.movieId, action.isRemote).collect { trailer ->
                if (trailer is DataResponse.Success<*>) {
                    trailer.body()?.let {
                        _trailer.postValue(it.filterMainTrailer())
                    }
                }
            }
            combine(
                config.provideImages.invoke(action.movieId, action.isRemote),
                config.provideTrailer.invoke(action.movieId, action.isRemote)
            ) { images, trailer ->
                if (images is DataResponse.Success<*>) {
                    images.body()?.let {
                        _images.postValue(it)
                    }
                }
                if (trailer is DataResponse.Success<*>) {
                    trailer.body()?.let {
                        _trailer.postValue(it.filterMainTrailer())
                    }
                }
                //TODO handle other DataResponse types
            }
        }
    }
}