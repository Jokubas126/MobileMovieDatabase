package com.example.moviesearcher.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL

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
        .override(SIZE_ORIGINAL)
        .into(imageView)
}

@BindingAdapter("android:imageUrl")
fun loadUrlImage(imageView: ImageView, imagePath: String?) {
    if (!imagePath.isNullOrBlank())
        loadImageFromUrl(imageView, imagePath, getProgressDrawable(imageView.context))
}

private fun loadImageFromBitmap(imageView: ImageView, bitmap: Bitmap?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(android.R.drawable.screen_background_light_transparent)
    Glide.with(imageView.context)
        .setDefaultRequestOptions(options)
        .load(bitmap)
        .override(SIZE_ORIGINAL)
        .into(imageView)
}

@BindingAdapter("android:imageBitmap")
fun loadBitmapImage(imageView: ImageView, bitmap: Bitmap?){
    if (imageView.drawable == null)
        loadImageFromBitmap(imageView, bitmap, getProgressDrawable(imageView.context))
}