package com.example.moviesearcher.ui.details.overview

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.moviesearcher.R
import com.example.moviesearcher.databinding.FragmentMovieOverviewBinding
import com.example.moviesearcher.model.data.MovieOld
import com.example.moviesearcher.util.KEY_MOVIE_ID
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_movie_overview.*

class OverviewFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var fragmentView: FragmentMovieOverviewBinding
    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentView = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_overview, container, false)
        return fragmentView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigationView = bottom_navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        viewModel = ViewModelProvider(this).get(OverviewViewModel::class.java)
        viewModel.fetch(activity as Activity, arguments)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentMovie.observe(viewLifecycleOwner, Observer { movie: MovieOld? ->
            if (movie != null) {
                //fragmentView.movie = movie
                information_layout.visibility = View.VISIBLE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            if (isLoading != null) {
                progress_bar.visibility =
                        if (isLoading)
                            View.VISIBLE
                        else View.GONE
                if (isLoading) information_layout.visibility = View.GONE
            }
        })
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.media_menu_item -> if (arguments != null) {
                val action: NavDirections = OverviewFragmentDirections.actionMovieMedia(arguments!!.getInt(KEY_MOVIE_ID))
                Navigation.findNavController(bottomNavigationView).navigate(action)
            }
            R.id.cast_menu_item -> if (arguments != null) {
                val action: NavDirections = OverviewFragmentDirections.actionMovieCast(arguments!!.getInt(KEY_MOVIE_ID))
                Navigation.findNavController(bottomNavigationView).navigate(action)
            }
        }
        return false
    }
}