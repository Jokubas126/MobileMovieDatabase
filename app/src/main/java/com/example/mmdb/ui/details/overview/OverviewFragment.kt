package com.example.mmdb.ui.details.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mmdb.R
import com.example.mmdb.databinding.FragmentMovieOverviewBinding
import com.example.mmdb.model.data.Movie
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_movie_overview.*
import kotlinx.android.synthetic.main.fragment_movie_overview.loading_error_text_view
import kotlinx.android.synthetic.main.fragment_movies_grid.*

class OverviewFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentView: FragmentMovieOverviewBinding
    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_overview, container, false)
        return fragmentView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            OverviewViewModelFactory(activity!!.application, arguments)
        ).get(OverviewViewModel::class.java)

        observeViewModel()
        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    private fun observeViewModel() {
        viewModel.currentMovie.observe(viewLifecycleOwner, Observer { movie ->
            movie?.let {
                fragmentView.movie = movie
                information_layout.visibility = View.VISIBLE
                loading_error_text_view.visibility = View.GONE
            } ?: run {
                loading_error_text_view.visibility = View.VISIBLE
            }
            progress_bar.visibility = View.GONE
        })
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        return viewModel.onNavigationItemSelected(bottom_navigation, menuItem)
    }
}