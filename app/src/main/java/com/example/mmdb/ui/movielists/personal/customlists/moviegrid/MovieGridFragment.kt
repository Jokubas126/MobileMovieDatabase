package com.example.mmdb.ui.movielists.personal.customlists.moviegrid

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
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
import com.example.mmdb.model.data.Movie
import com.example.mmdb.ui.movielists.MovieGridAdapter
import com.example.mmdb.util.SNACKBAR_LENGTH_LONG_MS
import com.example.mmdb.util.getMovieGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movies_grid.*

class MovieGridFragment : Fragment(), MovieGridAdapter.ItemClickListener,
    MovieGridAdapter.PersonalListDeleteListener, MovieGridAdapter.WatchlistActionListener {

    private lateinit var viewModel: MovieGridViewModel
    private val gridAdapter = MovieGridAdapter(
        View.GONE,
        View.VISIBLE,
        View.VISIBLE
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
        viewModel = ViewModelProvider(
            this,
            MovieGridViewModelFactory(activity!!.application, arguments)
        ).get(MovieGridViewModel::class.java)
        observeViewModel()
        setupRecyclerView()
        refresh_layout.setOnRefreshListener {
            viewModel.refresh()
            refresh_layout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.movieList.observe(viewLifecycleOwner, Observer { movieList ->
            movieList?.let {
                gridAdapter.updateMovieList(it)
                movie_recycler_view.visibility = View.VISIBLE
            }
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

        gridAdapter.setItemClickListener(this)
        gridAdapter.setWatchlistActionListener(this)
        gridAdapter.setPersonalListDeleteListener(this)
    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
            (activity as AppCompatActivity).supportActionBar?.title =
                MovieGridFragmentArgs.fromBundle(it).movieListTitle
        }
    }

    override fun onPause() {
        super.onPause()
        layoutManager?.let { state = it.onSaveInstanceState() }
    }

    override fun onMovieClick(view: View, movie: Movie) {
        viewModel.onMovieClicked(view, movie)
    }

    override fun onDeleteClicked(view: View, movie: Movie, position: Int) {
        gridAdapter.movieList.removeAt(position)
        gridAdapter.notifyItemRemoved(position)
        var restored = false
        Snackbar.make(view, R.string.movie_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                restored = true
                gridAdapter.movieList.add(position, movie)
                gridAdapter.notifyItemInserted(position)
            }.show()

        Handler().postDelayed({
            if (!restored)
                viewModel.deleteMovie(movie)
        }, SNACKBAR_LENGTH_LONG_MS.toLong())
    }

    override fun onWatchlistCheckChanged(movie: Movie) {
        viewModel.updateWatchlist(movie)
    }
}
