package com.jokubas.mmdb.ui_kit.bindingclasses

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

sealed class ImageBind {

    class ByResource(@DrawableRes val resourceId: Int): ImageBind()
}

@BindingAdapter("android:src")
fun ImageView.setImageBind(imageBind: ImageBind?) {
    when(imageBind) {
        is ImageBind.ByResource -> setImageResource(imageBind.resourceId)
        null -> setImageDrawable(null)
    }
}