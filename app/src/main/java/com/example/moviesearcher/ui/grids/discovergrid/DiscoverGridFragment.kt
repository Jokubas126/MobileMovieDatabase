package com.example.moviesearcher.ui.grids.discovergrid

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.ui.GridAdapter
import com.example.moviesearcher.util.stringListToString
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_movies_grid.*

class DiscoverGridFragment : Fragment(), GridAdapter.ItemClickListener,
    GridAdapter.PersonalListActionListener {

    private lateinit var viewModel: DiscoverGridViewModel
    private val gridAdapter = GridAdapter(View.VISIBLE, View.VISIBLE, View.GONE)

    private var isDown = true

    private var layoutManager: StaggeredGridLayoutManager? = null
    private var state: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(DiscoverGridViewModel::class.java)
        viewModel.fetch(arguments)

        setupRecyclerView()

        if (state != null) layoutManager!!.onRestoreInstanceState(state)

        observeViewModel()

        movie_recycler_view!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && isDown) {
                    isDown = false
                    viewModel.addData()
                    isDown = true
                }
            }
        })
        refresh_layout!!.setOnRefreshListener {
            viewModel.refresh()
            refresh_layout!!.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies: List<Movie>? ->
            if (movies != null) {
                gridAdapter.updateMovieList(movies)
                movie_recycler_view!!.visibility = View.VISIBLE
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { isError: Boolean? ->
            if (isError != null)
                loading_error_text_view!!.visibility =
                    if (isError)
                        View.VISIBLE
                    else View.GONE
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading: Boolean? ->
            if (isLoading != null) {
                progress_bar_loading_movie_list!!.visibility =
                    if (isLoading)
                        View.VISIBLE
                    else View.GONE
                if (isLoading)
                    loading_error_text_view!!.visibility = View.GONE
            }
        })
    }

    private fun setupRecyclerView() {
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        movie_recycler_view!!.layoutManager = layoutManager
        movie_recycler_view!!.itemAnimator = DefaultItemAnimator()
        movie_recycler_view!!.adapter = gridAdapter

        gridAdapter.setItemClickListener(this)
        gridAdapter.setPersonalListActionListener(this)
    }

    override fun onResume() {
        super.onResume()
        setupTitle()
    }

    private fun setupTitle() {
        val args = DiscoverGridFragmentArgs.fromBundle(arguments!!)
        var title = stringListToString(args.discoverNameArray.toList())
        if (title.isBlank() || title == "null")
            title = args.startYear + " - " + args.endYear
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.discover_fragment) + title
    }

    override fun onPause() {
        super.onPause()
        state = layoutManager!!.onSaveInstanceState()
    }

    override fun onMovieClick(view: View, movie: Movie) {
        viewModel.onMovieClicked(view, movie)
    }

    override fun onWatchlistCheckChanged(movie: Movie) {
        viewModel.updateWatchlist(movie)
    }

    override fun onPlaylistAdd(movie: Movie) {
        viewModel.onPlaylistAddCLicked(
            movie,
            (activity as AppCompatActivity).nav_host_fragment.requireView()
        ) // give nav_host_fragment because it's tide to activity's lifecycle and in this app structure is always active
    }
}
