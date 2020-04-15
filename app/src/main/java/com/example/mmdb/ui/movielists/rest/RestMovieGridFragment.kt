package com.example.mmdb.ui.movielists.rest

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mmdb.R
import com.example.mmdb.model.data.Movie
import com.example.mmdb.ui.movielists.MovieGridAdapter
import com.example.mmdb.ui.movielists.MovieGridAdapter.ItemClickListener
import com.example.mmdb.util.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_movies_grid.*
import java.util.*

@ExperimentalStdlibApi
class RestMovieGridFragment : Fragment(), ItemClickListener,
    MovieGridAdapter.PersonalListActionListener,
    MovieGridAdapter.WatchlistActionListener {

    private lateinit var viewModel: RestMovieGridViewModel
    private val gridAdapter = MovieGridAdapter(
        View.VISIBLE,
        View.VISIBLE,
        View.GONE
    )

    private var isScrolledDown = true

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
            RestMovieGridViewModelFactory(
                activity!!.application,
                arguments
            )
        ).get(RestMovieGridViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        refresh_layout.setOnRefreshListener {
            viewModel.refresh()
            refresh_layout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let {
                gridAdapter.updateMovieList(it)
                movie_recycler_view.visibility = View.VISIBLE
                if (isScrolledDown) isScrolledDown = false
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                loading_error_text_view.visibility =
                    if (it)
                        View.VISIBLE
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
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        movie_recycler_view.layoutManager = layoutManager
        state?.let { layoutManager?.onRestoreInstanceState(it) }
        movie_recycler_view.itemAnimator = DefaultItemAnimator()
        movie_recycler_view.adapter = gridAdapter

        gridAdapter.setItemClickListener(this)
        gridAdapter.setWatchlistActionListener(this)
        gridAdapter.setPersonalListActionListener(this)

        movie_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !isScrolledDown) {
                    isScrolledDown = true
                    viewModel.addData()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setupTitle()
    }

    private fun setupTitle() {
        val args = RestMovieGridFragmentArgs.fromBundle(arguments!!)
        val title =
            when (args.movieGridType) {
                TYPE_MOVIE_GRID ->
                    args.keyCategory?.let {
                        val title = StringBuilder()
                        val array = it.split("_").toTypedArray()
                        for (stringPart in array)
                            title.append(stringPart.capitalize(Locale.getDefault())).append(" ")
                        title.append("Movies")
                    } ?: run {
                        KEY_POPULAR.capitalize(Locale.ROOT) + " Movies"
                    }
                SEARCH_MOVIE_GRID -> args.searchQuery
                DISCOVER_MOVIE_GRID -> {
                   /* var title = args.startYear?: run { "∞" }
                    title += "-" + args.endYear*/
                    var title = args.discoverNameArray?.let {
                        stringListToString(args.discoverNameArray!!.toList())
                    }
                    title
                }
                else -> ""
            }
        (activity as AppCompatActivity).supportActionBar?.title = title.toString()
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