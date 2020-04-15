package com.example.mmdb.util

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import java.io.File

// ---------------- Image related -------------//

private fun getProgressDrawable(context: Context): CircularProgressDrawable {
    val cpd = CircularProgressDrawable(context)
    cpd.strokeWidth = 10f
    cpd.centerRadius = 50f
    cpd.start()
    return cpd
}

private fun loadImageFromUrl(
    imageView: ImageView,
    url: String?,
    progressDrawable: CircularProgressDrawable
) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(android.R.drawable.screen_background_light_transparent)
    Glide.with(imageView.context)
        .setDefaultRequestOptions(options)
        .load(BASE_IMAGE_URL + url)
        .override(SIZE_ORIGINAL)
        .into(imageView)
}

private fun loadImageFromUri(
    imageView: ImageView,
    uriString: String,
    progressDrawable: CircularProgressDrawable
) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(android.R.drawable.screen_background_light_transparent)
    Glide.with(imageView.context)
        .setDefaultRequestOptions(options)
        .load(File(Uri.parse(uriString).path!!))
        .override(SIZE_ORIGINAL)
        .into(imageView)
}

@BindingAdapter(value = ["imageUrl", "imageUriString"], requireAll = false)
fun loadImage(imageView: ImageView, imageUrl: String?, imageUriString: String?) {
    if (!imageUrl.isNullOrBlank())
        loadImageFromUrl(imageView, imageUrl, getProgressDrawable(imageView.context))
    else
        imageUriString?.let {
            loadImageFromUri(imageView, it, getProgressDrawable(imageView.context))
        }
}
