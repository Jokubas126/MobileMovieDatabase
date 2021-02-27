package com.jokubas.mmdb.util.extensions

import androidx.databinding.BindingAdapter
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@BindingAdapter("videoKey")
fun YouTubePlayerView.setVideoKey(videoKey: String?) {
    videoKey?.let {
        getYouTubePlayerWhenReady(
            object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoKey, 0f)
                }
            }
        )
    }
}