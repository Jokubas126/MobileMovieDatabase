package com.example.mmdb.ui.movielists.moviegrid

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mmdb.databinding.FragmentMoviesGridBinding
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.config
import com.example.mmdb.ui.movielists.MovieGridAdapter
import com.example.mmdb.ui.movielists.rest.MovieGridFragmentConfig
import kotlinx.android.synthetic.main.fragment_movies_grid.*

object MovieGridFragmentArgs : ConfigFragmentArgs<MovieGridFragmentAction, MovieGridFragmentConfig>()

class MovieGridFragment : Fragment() {

    private val gridAdapter = MovieGridAdapter()

    private val action: MovieGridFragmentAction by action()
    private val config: MovieGridFragmentConfig by config()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMoviesGridBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).apply {
            viewModel = ViewModelProvider(
                this@MovieGridFragment,
                MovieGridViewModelFactory(action, config, lifecycle)
            ).get(MovieGridViewModel::class.java)

        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_layout.setOnRefreshListener {
            //viewModel.refresh()
            refresh_layout.isRefreshing = false
        }
    }

//    override fun onMovieClick(view: View, movie: Movie) {
//        viewModel.onMovieClicked(view, movie)
//    }
//
//    override fun onDeleteClicked(view: View, movie: Movie, position: Int) {
//        gridAdapter.movieList.removeAt(position)
//        gridAdapter.notifyItemRemoved(position)
//        var restored = false
//        Snackbar.make(view, R.string.movie_deleted, Snackbar.LENGTH_LONG)
//            .setAction(R.string.undo) {
//                restored = true
//                gridAdapter.movieList.add(position, movie)
//                gridAdapter.notifyItemInserted(position)
//            }.show()
//
//        Handler().postDelayed({
//            if (!restored)
//                viewModel.deleteMovie(movie)
//        }, SNACKBAR_LENGTH_LONG_MS.toLong())
//    }
}
