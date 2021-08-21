package com.jokubas.mmdb.moviegrid.ui.grid

import android.os.Parcelable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.moviegrid.BR
import com.jokubas.mmdb.moviegrid.ui.movieitem.ItemMovieListViewModel
import com.jokubas.mmdb.moviegrid.R
import com.jokubas.mmdb.moviegrid.actions.MovieListType
import com.jokubas.mmdb.moviegrid.ui.movieitem.ItemMovieViewModel
import com.jokubas.mmdb.util.SaveState
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MovieGridContentViewModel(
    lifecycle: Lifecycle,
    private val movieListType: MovieListType,
    private val itemMovieListViewModel: ItemMovieListViewModel
) {

    private var state: Parcelable? = null
    val saveState: ObservableField<SaveState> = ObservableField(SaveState.Default)

    private val saveStateLifecycleListener = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                saveState.set(SaveState.Restore(state))
            }
            Lifecycle.Event.ON_PAUSE -> {
                saveState.set(SaveState.Save { newState ->
                    state = newState
                })
            }
            else -> {
            }
        }
    }

    init {
        lifecycle.addObserver(saveStateLifecycleListener)
    }

    val itemsMovie: ObservableList<ItemMovieViewModel> = ObservableArrayList<ItemMovieViewModel>().apply {
        addAll(itemMovieListViewModel.itemMovieViewModels)
    }
    val itemMoviesBinding: ItemBinding<ItemMovieViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_movie)

    fun updateMovieItems(
        newItemMovieListViewModel: ItemMovieListViewModel,
        watchlistMovies: List<WatchlistMovie>
    ) {
        when {
            itemsMovie.isEmpty() && newItemMovieListViewModel.itemMovieViewModels.isNotEmpty() ->
                itemsMovie.addAll(newItemMovieListViewModel.itemMovieViewModels)

            itemsMovie.zip(newItemMovieListViewModel.itemMovieViewModels)
                .none { (new, old) -> new.movie.id == old.movie.id } -> {
                itemsMovie.removeAll { true }
                itemsMovie.addAll(newItemMovieListViewModel.itemMovieViewModels)
            }

            else -> {
                itemsMovie.forEach { movieItem ->
                    movieItem.isInWatchlist.set(
                        watchlistMovies.any { watchlistMovie ->
                            movieItem.movie.id == watchlistMovie.movieId
                        }
                    )
                }

                if (movieListType is MovieListType.Remote.Watchlist)
                    itemsMovie.filter { !it.isInWatchlist.get() }.forEach {
                        itemsMovie.remove(it)
                    }
            }
        }
    }
}