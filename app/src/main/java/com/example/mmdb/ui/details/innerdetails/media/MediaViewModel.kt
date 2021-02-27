package com.example.mmdb.ui.details.innerdetails.media

import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.jokubas.mmdb.model.data.entities.Image
import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.Video
import com.jokubas.mmdb.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MediaViewModel(
    action: InnerDetailsAction.MediaAction,
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
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()

            when (val response = config.provideMediaInfo.invoke(action.movieIdWrapper)){
                is DataResponse.Success<*> -> {
                    (response.value as? MediaInfo)?.let {
                        _images.postValue(it.images)
                        _trailer.postValue(it.trailer)
                        progressManager.loaded()
                    } ?: progressManager.error()
                }
                is DataResponse.Error -> progressManager.error()
            }

        }
    }
}