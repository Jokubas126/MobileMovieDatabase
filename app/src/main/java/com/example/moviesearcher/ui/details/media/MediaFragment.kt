package com.example.moviesearcher.ui.details.media

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Video
import com.example.moviesearcher.util.YOUTUBE_API_KEY
import com.example.moviesearcher.util.KEY_MOVIE_ID
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.fragment_movie_media.*

class MediaFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var posterView: RecyclerView
    private lateinit var backdropView: RecyclerView

    private val youTubePlayerFragment = YouTubePlayerFragment.newInstance() // cannot use support fragment due to unknown reasons

    private lateinit var viewModel: MediaViewModel

    private val posterAdapter = ImageAdapter()
    private val backdropAdapter = ImageAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = bottom_navigation
        posterView = poster_recycler_view
        backdropView = backdrop_recycler_view

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        viewModel = ViewModelProviders.of(this).get(MediaViewModel::class.java)
        viewModel.fetch(activity as Activity, arguments)

        posterView.layoutManager = GridLayoutManager(context, 2)
        posterView.itemAnimator = DefaultItemAnimator()
        posterView.adapter = posterAdapter

        backdropView.layoutManager = LinearLayoutManager(context)
        backdropView.itemAnimator = DefaultItemAnimator()
        backdropView.adapter = backdropAdapter
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.trailer.observe(viewLifecycleOwner, Observer { trailer: Video? ->
            if (trailer != null) {
                video_name.text = trailer.name
                @Suppress("DEPRECATION")
                activity!!.fragmentManager.beginTransaction().replace(youtube_fragment.id, youTubePlayerFragment).commit()
                initializeYoutubePlayer(trailer.key)
            }
        })
        viewModel.posterList.observe(viewLifecycleOwner, Observer { posterList: List<String>? ->
            if (!posterList.isNullOrEmpty()) {
                posterAdapter.updateImagePathList(posterList)
                poster_layout.visibility = View.VISIBLE
            }
        })
        viewModel.backdropList.observe(viewLifecycleOwner, Observer { backdropList: List<String>? ->
            if (!backdropList.isNullOrEmpty()) {
                backdropAdapter.updateImagePathList(backdropList)
                backdrop_layout.visibility = View.VISIBLE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            if (isLoading != null) {
                progress_bar.visibility =
                        if (isLoading)
                            View.VISIBLE
                        else View.GONE
                information_layout.visibility =
                        if (isLoading)
                            View.GONE
                        else View.VISIBLE
            }
        })
    }

    private fun initializeYoutubePlayer(key: String) {
        youTubePlayerFragment.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                youTubePlayer.setFullscreen(false)
                youTubePlayer.setShowFullscreenButton(false)
                youTubePlayer.cueVideo(key)
                Log.d("YoutubePlayer", "onInitializationFailure: successfully to initialized")
            }
            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
                Log.d("YoutubePlayer", "onInitializationFailure: failed to initialize")
            }
        })
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.overview_menu_item -> if (arguments != null) {
                val action: NavDirections = MediaFragmentDirections.actionMovieOverview(arguments!!.getInt(KEY_MOVIE_ID))
                Navigation.findNavController(bottomNavigationView).navigate(action)
            }
            R.id.cast_menu_item -> if (arguments != null) {
                val action: NavDirections = MediaFragmentDirections.actionMovieCast(arguments!!.getInt(KEY_MOVIE_ID))
                Navigation.findNavController(bottomNavigationView).navigate(action)
            }
        }
        return false
    }
}