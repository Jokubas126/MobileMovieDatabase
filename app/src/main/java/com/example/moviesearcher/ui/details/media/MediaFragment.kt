package com.example.moviesearcher.ui.details.media

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Image
import com.example.moviesearcher.util.YOUTUBE_API_KEY
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.fragment_movie_media.*

class MediaFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val youTubePlayerFragment =
        YouTubePlayerFragment.newInstance() // cannot use support fragment

    private lateinit var viewModel: MediaViewModel

    private val posterAdapter = ImageAdapter()
    private val backdropAdapter = ImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            MediaViewModelFactory(activity!!.application, arguments)
        ).get(MediaViewModel::class.java)

        setupRecyclerViews()
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.trailer.observe(viewLifecycleOwner, Observer { video ->
            video?.let {
                video_name.text = it.name
                trailer_layout.visibility = View.VISIBLE
                @Suppress("DEPRECATION")
                activity!!.fragmentManager.beginTransaction()
                    .replace(youtube_fragment.id, youTubePlayerFragment).commit()
                initializeYoutubePlayer(it.key)
            }
        })
        viewModel.images?.observe(viewLifecycleOwner, Observer { images ->
            images?.let {
                updatePosters(it.posterList)
                updateBackdrops(it.backdropList)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            isLoading?.let {
                progress_bar.visibility =
                    if (it) View.VISIBLE
                    else View.GONE
                information_layout.visibility =
                    if (it) View.GONE
                    else View.VISIBLE
            }
        })
    }

    private fun initializeYoutubePlayer(key: String) {
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
                    Log.d("YoutubePlayer", "onInitializationFailure: successfully initialized")
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    Log.d("YoutubePlayer", "onInitializationFailure: failed to initialize")
                }
            })
    }

    private fun setupRecyclerViews() {
        poster_recycler_view.layoutManager = GridLayoutManager(context, 2)
        poster_recycler_view.itemAnimator = DefaultItemAnimator()
        poster_recycler_view.adapter = posterAdapter

        backdrop_recycler_view.layoutManager = LinearLayoutManager(context)
        backdrop_recycler_view.itemAnimator = DefaultItemAnimator()
        backdrop_recycler_view.adapter = backdropAdapter
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        return viewModel.onNavigationItemSelected(bottom_navigation, menuItem)
    }

    private fun updatePosters(posterList: List<Image>?) {
        if (!posterList.isNullOrEmpty()) {
            posterAdapter.updateImageList(posterList)
            poster_layout.visibility = View.VISIBLE
        }
    }

    private fun updateBackdrops(backdropList: List<Image>?) {
        if (!backdropList.isNullOrEmpty()) {
            backdropAdapter.updateImageList(backdropList)
            backdrop_layout.visibility = View.VISIBLE
        }
    }
}