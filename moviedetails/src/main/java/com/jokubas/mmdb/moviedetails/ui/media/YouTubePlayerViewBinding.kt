package com.jokubas.mmdb.moviedetails.ui.media

import androidx.databinding.BindingAdapter
import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@BindingAdapter("lifecycle")
fun YouTubePlayerView.beLifecycleObserver(lifecycle: Lifecycle?) {
    lifecycle?.addObserver(this)
}