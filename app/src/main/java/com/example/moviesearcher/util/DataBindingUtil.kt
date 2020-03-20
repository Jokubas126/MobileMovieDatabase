package com.example.moviesearcher.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

// ---------------- Image related -------------//

private fun getProgressDrawable(context: Context): CircularProgressDrawable {
    val cpd = CircularProgressDrawable(context)
    cpd.strokeWidth = 10f
    cpd.centerRadius = 50f
    cpd.start()
    return cpd
}

private fun loadImageFromUrl(imageView: ImageView, imagePath: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(android.R.drawable.screen_background_light_transparent)
    Glide.with(imageView.context)
        .setDefaultRequestOptions(options)
        .load(BASE_IMAGE_URL + imagePath)
        .into(imageView)
}

@BindingAdapter("android:imageUrl")
fun loadUrlImage(imageView: ImageView, imagePath: String?) {
    loadImageFromUrl(imageView, imagePath, getProgressDrawable(imageView.context))
}

private fun loadImageFromByteArray(imageView: ImageView, byteArray: ByteArray?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(android.R.drawable.screen_background_light_transparent)
    Glide.with(imageView.context)
        .setDefaultRequestOptions(options)
        .load(byteArray)
        .into(imageView)
}

@BindingAdapter("android:imageByteArray")
fun loadByteArrayImage(imageView: ImageView, byteArray: ByteArray?){
    loadImageFromByteArray(imageView, byteArray, getProgressDrawable(imageView.context))
}