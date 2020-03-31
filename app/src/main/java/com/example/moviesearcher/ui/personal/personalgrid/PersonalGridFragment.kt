package com.example.moviesearcher.ui.personal.personalgrid

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
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.ui.GridAdapter
import com.example.moviesearcher.util.SNACKBAR_LENGTH_LONG_MS
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movies_grid.*

class PersonalGridFragment : Fragment(), GridAdapter.ItemClickListener,
    GridAdapter.PersonalListActionListener {

    private lateinit var viewModel: PersonalGridViewModel
    private val gridAdapter = GridAdapter(View.GONE, View.VISIBLE, View.VISIBLE)

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
        viewModel = ViewModelProvider(this).get(PersonalGridViewModel::class.java)
        viewModel.fetch(arguments)

        setupRecyclerView()

        if (state != null)
            layoutManager!!.onRestoreInstanceState(state)

        observeViewModel()

        refresh_layout!!.setOnRefreshListener {
            viewModel.refresh()
            refresh_layout!!.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.movieList?.observe(viewLifecycleOwner, Observer {
            viewModel.movies(it.movieIdList)
                ?.observe(viewLifecycleOwner, Observer { movies: List<Movie>? ->
                    gridAdapter.updateMovieList(movies)
                    movie_recycler_view!!.visibility = View.VISIBLE
                })
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
        val args: PersonalGridFragmentArgs? = PersonalGridFragmentArgs.fromBundle(arguments!!)
        if (args != null)
        (activity as AppCompatActivity).supportActionBar?.title = args.movieListTitle
    }

    override fun onPause() {
        super.onPause()
        state = layoutManager!!.onSaveInstanceState()
    }

    override fun onMovieClick(view: View, movie: Movie) {
        viewModel.onMovieClicked(view, movie)
    }

    override fun onPlaylistAdd(movie: Movie) {
    }

    override fun onDeleteClicked(view: View, movie: Movie) {
        val oldList = mutableListOf<Movie>()
        val newList = mutableListOf<Movie>()
        oldList.addAll(gridAdapter.movieList)
        newList.addAll(oldList)
        newList.remove(movie)
        gridAdapter.updateMovieList(newList)
        var restored = false
        Snackbar.make(view, R.string.movie_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                restored = true
                gridAdapter.updateMovieList(oldList)
            }.show()

        Handler().postDelayed({
            if (!restored)
                viewModel.deleteMovie(movie)
        }, SNACKBAR_LENGTH_LONG_MS.toLong())
    }
}
