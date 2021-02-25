package com.example.mmdb.ui.details.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mmdb.databinding.FragmentMovieMediaBinding
import kotlinx.android.synthetic.main.fragment_movie_media.bottom_navigation

class MediaFragment : Fragment() {

/*    private val args by lazy {
        MovieDetailsArgs.fromBundle(arguments ?: Bundle())
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMovieMediaBinding.inflate(inflater, container, false)
        /*binding.viewModel =
            ViewModelProvider(
                this,
                MediaViewModelFactory(activity!!.application, args.movieLocalId, args.movieRemoteId)
            ).get(MediaViewModel::class.java)*/
        binding.lifecycleOwner = this
        binding.fragmentManager = activity!!.fragmentManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            /*when (menuItem.itemId) {
                R.id.overview_menu_item -> {
                    MediaFragmentDirections.actionMovieOverview().apply {
                        movieRemoteId = args.movieRemoteId
                        movieLocalId = args.movieLocalId
                        findNavController().navigate(this)
                    }
                }
                R.id.credits_menu_item -> {
                    MediaFragmentDirections.actionMovieCredits().apply {
                        movieRemoteId = args.movieRemoteId
                        movieLocalId = args.movieLocalId
                        findNavController().navigate(this)
                    }
                }
            }*/
            true
        }
    }
}