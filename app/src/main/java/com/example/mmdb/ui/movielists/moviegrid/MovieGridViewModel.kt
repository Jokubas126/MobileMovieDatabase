package com.example.mmdb.ui.movielists.moviegrid

import android.os.Parcelable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.details.IdWrapper
import com.example.mmdb.ui.movielists.ItemMovieViewModel
import com.example.mmdb.ui.movielists.rest.DiscoverSelectionViewModel
import com.example.mmdb.ui.movielists.rest.PageSelectionListViewModel
import com.example.mmdb.ui.movielists.rest.MovieGridFragmentConfig
import com.example.mmdb.ui.movielists.toItemMovieViewModel
import com.jokubas.mmdb.util.SaveState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MovieGridViewModel(
    action: MovieGridFragmentAction,
    config: MovieGridFragmentConfig,
    lifecycle: Lifecycle
) : ViewModel() {

    private var state: Parcelable? = null
    val saveState: ObservableField<SaveState> = ObservableField(SaveState.Default)

    val progressManager = ProgressManager()

    val pageSelectionListViewModel = ObservableField<PageSelectionListViewModel>()
    val discoverSelectionViewModel: DiscoverSelectionViewModel? =
        if (action.movieListType is MovieListType.Discover) {
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

    init {
        CoroutineScope(Dispatchers.IO).launch {
            progressManager.loading()
            config.provideMovies(
                action.movieListType,
                1
            ).apply {
                val itemMovieViewModelList = movieList.mapIndexed { index, movie ->
                    movie.toItemMovieViewModel(
                        position = index,
                        itemMovieEventListener = config.itemMovieEventListener.invoke(IdWrapper.Remote(movie.remoteId)),
                        page = page
                    )
                }
                withContext(Dispatchers.Main) {
                    itemsMovie.removeAll{ true }
                    itemsMovie.addAll(itemMovieViewModelList)
                }
            }
        }
        lifecycle.addObserver(saveStateLifecycleListener)
    }
}