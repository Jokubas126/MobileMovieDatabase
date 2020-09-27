package com.example.mmdb.ui.details.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mmdb.MovieDetailsArgs
import com.example.mmdb.R
import com.example.mmdb.databinding.FragmentMovieOverviewBinding
import kotlinx.android.synthetic.main.fragment_movie_overview.*

class OverviewFragment : Fragment() {

    private lateinit var viewModel: OverviewViewModel
    private val args by lazy { MovieDetailsArgs.fromBundle(arguments ?: Bundle()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentMovieOverviewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            OverviewViewModelFactory(
                activity!!.application,
                args.movieLocalId,
                args.movieRemoteId
            )
        ).get(OverviewViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.media_menu_item ->
                    OverviewFragmentDirections.actionMovieMedia().apply {
                        movieRemoteId = args.movieRemoteId
                        movieLocalId = args.movieLocalId
                        findNavController().navigate(this)
                    }
                R.id.credits_menu_item ->
                    OverviewFragmentDirections.actionMovieCredits().apply {
                        movieRemoteId = args.movieRemoteId
                        movieLocalId = args.movieLocalId
                        findNavController().navigate(this)
                    }
            }
            true
        }
    }
}