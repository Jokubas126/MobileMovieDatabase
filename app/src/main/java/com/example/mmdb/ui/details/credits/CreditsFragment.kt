package com.example.mmdb.ui.details.credits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mmdb.databinding.FragmentMovieCreditsBinding
import kotlinx.android.synthetic.main.fragment_movie_credits.bottom_navigation

class CreditsFragment : Fragment() {
/*
    private val args by lazy {
        MovieDetailsArgs.fromBundle(arguments ?: Bundle())
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentMovieCreditsBinding.inflate(inflater, container, false)

/*        binding.viewModel =
            ViewModelProvider(
                this,
                CreditsViewModelFactory(
                    activity!!.application,
                    args.movieLocalId,
                    args.movieRemoteId
                )
            ).get(CreditsViewModel::class.java)*/
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            /*when (menuItem.itemId) {
                R.id.media_menu_item -> {
                    CreditsFragmentDirections.actionMovieMedia().apply {
                        movieRemoteId = args.movieRemoteId
                        movieLocalId = args.movieLocalId
                        findNavController().navigate(this)
                    }
                }
                R.id.overview_menu_item -> {
                    CreditsFragmentDirections.actionMovieOverview().apply {
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