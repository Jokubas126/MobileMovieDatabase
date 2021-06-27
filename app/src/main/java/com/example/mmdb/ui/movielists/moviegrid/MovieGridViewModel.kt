package com.example.mmdb.ui.movielists.moviegrid

import android.os.Parcelable
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.movielists.discover.DiscoverSelectionViewModel
import com.example.mmdb.ui.movielists.pageselection.PageSelectionListViewModel
import com.jokubas.mmdb.util.SaveState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.ItemBinding

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

    val itemsMovie = ObservableArrayList<ItemMovieViewModel>()
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

    init {
        loadMovieList(pageSelectionListViewModel.currentPage.get())
        lifecycle.addObserver(saveStateLifecycleListener)
    }

    fun loadMovieList(page: Int) {
        progressManager.loading()
        CoroutineScope(Dispatchers.IO).launch {
            config.provideMovies(
                action.movieListType,
                page
            ).apply {

                val watchlist = config.provideWatchlist.invoke()

                val itemMovieViewModelList = movieList.mapIndexed { index, movie ->
                    movie.toItemMovieViewModel(
                        position = index,
                        itemMovieEventListener = config.itemMovieEventListener.invoke(
                            movie.id,
                            action.movieListType is MovieListType.Remote
                        ),
                        page = page,
                        isInWatchlist = watchlist.find { it.movieId == movie.id } != null
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