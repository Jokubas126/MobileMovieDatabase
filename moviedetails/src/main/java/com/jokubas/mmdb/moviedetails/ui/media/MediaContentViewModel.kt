package com.jokubas.mmdb.moviedetails.ui.media

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import com.jokubas.mmdb.moviedetails.model.entities.Image
import com.jokubas.mmdb.moviedetails.model.entities.Images
import com.jokubas.mmdb.moviedetails.model.entities.Video
import com.jokubas.mmdb.moviedetails.BR
import com.jokubas.mmdb.moviedetails.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MediaContentViewModel(
    val lifecycle: Lifecycle,
    initialTrailer: Video? = null,
    initialImages: Images? = null
) {

    val trailer: ObservableField<Video?> = ObservableField(initialTrailer)
    val images: ObservableField<Images?> = ObservableField(initialImages)

    val imageBinding: ItemBinding<Image> = ItemBinding.of(BR.image, R.layout.item_image)

    fun updateTrailer(newTrailer: Video?) {
        trailer.set(newTrailer)
    }

    fun updateImages(newImages: Images?) {
        images.set(newImages)
    }
}