package com.example.mmdb.ui.movielists.personal.watchlist

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mmdb.R
import com.example.mmdb.ui.movielists.MovieGridAdapter
import com.jokubas.mmdb.util.getMovieGridLayoutManager
import com.jokubas.mmdb.model.data.entities.Movie
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_movies_grid.*

class WatchlistFragment : Fragment(), MovieGridAdapter.ItemClickListener,
    MovieGridAdapter.PersonalListActionListener, MovieGridAdapter.WatchlistActionListener {

    private lateinit var viewModel: WatchlistViewModel

    private val gridAdapter = MovieGridAdapter(
        View.VISIBLE,
        View.VISIBLE,
        View.GONE
    )

    private var layoutManager: StaggeredGridLayoutManager? = null
    private var state: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WatchlistViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        refresh_layout.setOnRefreshListener {
            viewModel.refresh()
            refresh_layout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            gridAdapter.updateMovieList(movies)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                loading_error_text_view.visibility =
                    if (it) View.VISIBLE
                    else View.GONE
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                progress_bar_loading_movie_list.visibility =
                    if (it) View.VISIBLE
                    else View.GONE
                if (it) loading_error_text_view.visibility = View.GONE
            }
        })
    }

    private fun setupRecyclerView() {
        layoutManager = getMovieGridLayoutManager(context) as StaggeredGridLayoutManager
        movie_recycler_view.layoutManager = layoutManager
        state?.let { layoutManager?.onRestoreInstanceState(it) }
        movie_recycler_view.itemAnimator = DefaultItemAnimator()
        movie_recycler_view.adapter = gridAdapter

        gridAdapter.setPersonalListActionListener(this)
        gridAdapter.setWatchlistActionListener(this)
        gridAdapter.setItemClickListener(this)
    }

    override fun onMovieClick(view: View, movie: Movie) {
        viewModel.onMovieClicked(view, movie)
    }

    override fun onPause() {
        super.onPause()
        layoutManager?.let { state = it.onSaveInstanceState() }
    }

    override fun onWatchlistCheckChanged(movie: Movie) {
        viewModel.updateWatchlist(movie)
    }

    override fun onPlaylistAdd(movie: Movie) {
        viewModel.onPlaylistAddCLicked(
            movie,
            (activity as AppCompatActivity).nav_host_fragment.requireView()
        )
    }
}
