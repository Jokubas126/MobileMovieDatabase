package com.example.mmdb.ui.details.innerdetails.media

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import com.example.mmdb.BR
import com.example.mmdb.R
import com.jokubas.mmdb.model.data.entities.Image
import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.Video
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