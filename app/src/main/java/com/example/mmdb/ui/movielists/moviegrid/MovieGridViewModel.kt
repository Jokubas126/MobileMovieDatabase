package com.example.mmdb.ui.movielists.moviegrid

import android.os.Parcelable
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.movielists.discover.DiscoverSelectionViewModel
import com.example.mmdb.ui.movielists.pageselection.PageSelectionListViewModel
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.SaveState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MovieGridViewModel(
    private val action: MovieGridFragmentAction,
    private val config: MovieGridFragmentConfig,
    lifecycle: Lifecycle
) : ViewModel() {

    private var state: Parcelable? = null
    val saveState: ObservableField<SaveState> = ObservableField(SaveState.Default)

    val progressManager = ProgressManager()

    val pageSelectionListViewModel = PageSelectionListViewModel(viewModelScope + Dispatchers.IO)

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
            else -> {
            }
        }
    }

    val refreshEventListener = {
        loadMovieList()
    }

    init {
        lifecycle.addObserver(saveStateLifecycleListener)
        loadMovieList()
    }

    fun loadMovieList() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            progressManager.error()
            Log.e("MovieGridViewModel", "loadMovieList: ", throwable)
        }) {

            combine(
                config.provideMovies.invoke(
                    action.movieListType,
                    pageSelectionListViewModel.currentPage
                ),
                config.provideWatchlist.invoke()
            ) { movies, watchlistMovies ->
                MovieListData(movies, watchlistMovies)
            }.collect { movieListData: MovieListData ->
                movieListData.movieResultDataResponse.body()?.let { movieResults ->
                    val itemMovieListViewModel = ItemMovieListViewModel(
                        movieListType = action.movieListType,
                        itemMovieEventListener = { movieId ->
                            config.itemMovieEventListener.invoke(
                                movieId,
                                action.movieListType is MovieListType.Remote
                            )
                        },
                        watchlistMovieIds = movieListData.watchlistMovies.map { it.movieId },
                        movieResults = movieResults
                    )

                    withContext(Dispatchers.Main) {
                        pageSelectionListViewModel.update(
                            currentPage = movieResults.page,
                            totalPages = movieResults.totalPages
                        )
                        updateMovieItems(
                            newItemMovieListViewModel = itemMovieListViewModel,
                            watchlistMovies = movieListData.watchlistMovies
                        )
                        progressManager.success()
                    }
                } ?: run {
                    when (movieListData.movieResultDataResponse) {
                        is DataResponse.Error,
                        is DataResponse.Empty -> progressManager.error()
                        else -> progressManager.loading()
                    }
                }
            }
        }
    }

    private fun updateMovieItems(
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

                if (action.movieListType is MovieListType.Remote.Watchlist)
                    itemsMovie.filter { !it.isInWatchlist.get() }.forEach {
                        itemsMovie.remove(it)
                    }
            }
        }
    }

    data class MovieListData(
        val movieResultDataResponse: DataResponse<MovieResults>,
        val watchlistMovies: List<WatchlistMovie>
    )
}