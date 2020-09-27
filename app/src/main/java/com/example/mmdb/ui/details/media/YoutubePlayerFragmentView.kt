package com.example.mmdb.ui.details.media

import android.app.FragmentManager
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.jokubas.mmdb.util.YOUTUBE_API_KEY

class YoutubePlayerFragmentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var fragmentManager: FragmentManager? = null

    private val youTubePlayerFragment by lazy { YouTubePlayerFragment() }

    var key: String? = null
        set(value) {
            field = value
            initializeYoutubePlayer()
        }

    @Suppress("DEPRECATION")
    private fun initializeYoutubePlayer() {
        fragmentManager?.beginTransaction()?.replace(this.id, youTubePlayerFragment)?.let {
            it.commit()

            youTubePlayerFragment.initialize(
                YOUTUBE_API_KEY,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        youTubePlayer: YouTubePlayer,
                        b: Boolean
                    ) {
                        youTubePlayer.setFullscreen(false)
                        youTubePlayer.setShowFullscreenButton(false)
                        youTubePlayer.cueVideo(key)
                    }

                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        youTubeInitializationResult: YouTubeInitializationResult
                    ) {
                    }
                })
        }
    }
}