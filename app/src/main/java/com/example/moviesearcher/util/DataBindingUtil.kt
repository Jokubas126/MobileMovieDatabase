package com.example.moviesearcher.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

// ---------------- Image related -------------//
private fun loadImageFromUrl(imageView: ImageView, url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
            .placeholder(progressDrawable)
            .error(android.R.drawable.screen_background_light_transparent)
    Glide.with(imageView.context)
            .setDefaultRequestOptions(options)
            .load(url)
            .into(imageView)
}

private fun getProgressDrawable(context: Context): CircularProgressDrawable {
    val cpd = CircularProgressDrawable(context)
    cpd.strokeWidth = 10f
    cpd.centerRadius = 50f
    cpd.start()
    return cpd
}

@BindingAdapter("android:imageUrl")
fun loadUrlImage(imageView: ImageView, url: String?) {
    loadImageFromUrl(imageView, url, getProgressDrawable(imageView.context))
}