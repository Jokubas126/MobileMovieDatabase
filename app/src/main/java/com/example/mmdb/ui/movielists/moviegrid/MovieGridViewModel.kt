package com.example.mmdb.ui.movielists.moviegrid

import android.os.Parcelable
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.movielists.discover.DiscoverSelectionViewModel
import com.example.mmdb.ui.movielists.pageselection.PageSelectionListViewModel
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.SaveState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import kotlin.coroutines.CoroutineContext

class MovieGridViewModel(
    private val action: MovieGridFragmentAction,
    private val config: MovieGridFragmentConfig,
    lifecycle: Lifecycle
) : ViewModel() {

    private var state: Parcelable? = null
    val saveState: ObservableField<SaveState> = ObservableField(SaveState.Default)

    val progressManager = ProgressManager()

    val pageSelectionListViewModel = PageSelectionListViewModel(
        onSelected = { pageNumber ->
            loadMovieList(pageNumber)
        }
    )

    val discoverSelectionViewModel: DiscoverSelectionViewModel? =
        if (action.movieListType is MovieListType.Remote.Discover) {
            DiscoverSelectionViewModel(action.movieListType.discoverNameList)
        } else null

    val itemsMovie: ObservableList<ItemMovieViewModel> = ObservableArrayList()
    val itemMoviesBinding: ItemBinding<ItemMovieViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_movie)

    val saveStateLifecycleListener = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                saveState.set(SaveState.Restore(state))
            }
            Lifecycle.Event.ON_PAUSE -> {
                saveState.set(SaveState.Save { newState ->
                    state = newState
                })
            }
        }
    }

    val refreshEventListener = {
        loadMovieList(pageSelectionListViewModel.currentPage.get())
    }

    private val watchlist =
        config.provideWatchlist.invoke().asLiveData(Dispatchers.IO).apply {
            loadMovieList(pageSelectionListViewModel.currentPage.get())
            observeForever { watchlistMovies ->
                itemsMovie.forEach { movieItem ->
                    movieItem.isInWatchlist.set(
                        watchlistMovies.any { watchlistMovie ->
                            movieItem.movie.id == watchlistMovie.movieId
                        }
                    )
                }
                if (action.movieListType is MovieListType.Remote.Watchlist) {
                    itemsMovie.filter { !it.isInWatchlist.get() }.forEach {
                        itemsMovie.remove(it)
                    }
                }
            }
        }

    init {
        lifecycle.addObserver(saveStateLifecycleListener)
    }

    fun loadMovieList(page: Int) {
        progressManager.loading()
        CoroutineScope(Dispatchers.IO).launch {
            config.provideMovies(
                action.movieListType,
                page
            ).apply {

                val itemMovieViewModelList = movieList.mapIndexed { index, movie ->
                    movie.toItemMovieViewModel(
                        position = index,
                        itemMovieEventListener = config.itemMovieEventListener.invoke(
                            movie.id,
                            action.movieListType is MovieListType.Remote
                        ),
                        page = page,
                        isInWatchlist = watchlist.value?.find { it.movieId == movie.id } != null,
                        isRemote = action.movieListType is MovieListType.Remote
                    )
                }

                withContext(Dispatchers.Main) {
                    pageSelectionListViewModel.update(page, totalPages)
                    itemsMovie.removeAll { true }
                    itemsMovie.addAll(itemMovieViewModelList)
                    progressManager.success()
                }
            }
        }
    }
}