package com.example.mmdb.ui.details.innerdetails.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.databinding.FragmentMovieMediaBinding
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.example.mmdb.navigation.config
import kotlinx.android.synthetic.main.fragment_movie_media.*

object MediaFragmentArgs: ConfigFragmentArgs<InnerDetailsAction.MediaAction, MediaConfig>()

class MediaFragment : Fragment() {

    private val action: InnerDetailsAction.MediaAction by action()
    private val config: MediaConfig by config()

    private val mediaViewModel: MediaViewModel by lazy {
        ViewModelProvider(
            this,
            MediaViewModelFactory(
                action = action,
                config = config
            )
        ).get(MediaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMovieMediaBinding.inflate(inflater, container, false).apply {
        viewModel = mediaViewModel
        lifecycleOwner = this@MediaFragment
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(youtubePlayerView)
    }
}