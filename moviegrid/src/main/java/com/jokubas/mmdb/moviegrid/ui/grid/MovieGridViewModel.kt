package com.jokubas.mmdb.moviegrid.ui.grid

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokubas.mmdb.feedback_ui.LoadingViewModel
import com.jokubas.mmdb.feedback_ui.error.ErrorViewModel
import com.jokubas.mmdb.feedback_ui.error.GenericErrorViewModels
import com.jokubas.mmdb.moviegrid.BR
import com.jokubas.mmdb.moviegrid.ui.movieitem.ItemMovieListViewModel
import com.jokubas.mmdb.moviegrid.R
import com.jokubas.mmdb.moviegrid.actions.MovieGridFragmentAction
import com.jokubas.mmdb.moviegrid.actions.MovieListType
import com.jokubas.mmdb.moviegrid.ui.pageselection.PageSelectionListViewModel
import com.jokubas.mmdb.moviegrid.ui.discover.DiscoverSelectionViewModel
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.extensions.replaceAt
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class MovieGridViewModel(
    private val action: MovieGridFragmentAction,
    private val config: MovieGridFragmentConfig
) : ViewModel() {

    private val discoverSelectionViewModel: DiscoverSelectionViewModel? =
        (action.movieListType as? MovieListType.Remote.Discover)?.let {
            DiscoverSelectionViewModel(it.discoverNameList)
        }

    private val pageSelectionListViewModel =
        PageSelectionListViewModel(viewModelScope + Dispatchers.IO)

    val items: ObservableList<Any> = ObservableArrayList<Any>().apply {
        discoverSelectionViewModel?.let { add(it) }
        add(pageSelectionListViewModel)
        add(LoadingViewModel)
    }

    val itemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(LoadingViewModel::class.java, BR.viewModel, R.layout.loading_view)
        .map(ErrorViewModel::class.java, BR.viewModel, R.layout.error_view)
        .map(MovieGridContentViewModel::class.java, BR.viewModel, R.layout.movie_grid_content)
        .map(PageSelectionListViewModel::class.java, BR.viewModel, R.layout.page_selection_list)
        .map(DiscoverSelectionViewModel::class.java, BR.viewModel, R.layout.discover_selection_view)

    val refreshEventListener = {
        loadMovieList()
    }

    private val errorViewModel = GenericErrorViewModels.NetworkErrorViewModel {
        loadMovieList()
    }

    init {
        loadMovieList()
    }

    private fun loadMovieList() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch(Dispatchers.Main) {
                items.replaceAt(
                    index = items.lastIndex,
                    item = GenericErrorViewModels.Unknown
                )
            }
            Log.e("MovieGridViewModel", "loadMovieList: $throwable")
        }) {

            combine(
                config.provideMovies.invoke(
                    action.movieListType,
                    pageSelectionListViewModel.currentPage
                ),
                config.provideWatchlist.invoke()
            ) { movies, watchlistMovies -> movies to watchlistMovies }
                .collect { (movieResultResponse, watchlistMovies) ->
                    movieResultResponse.body()?.let { movieResults ->
                        val itemMovieListViewModel = ItemMovieListViewModel(
                            movieListType = action.movieListType,
                            itemMovieEventListener = { movieId ->
                                config.itemMovieEventListener.invoke(
                                    movieId,
                                    action.movieListType is MovieListType.Remote
                                )
                            },
                            watchlistMovieIds = watchlistMovies.map { it.movieId },
                            movieResults = movieResults
                        )

                        withContext(Dispatchers.Main) {
                            pageSelectionListViewModel.update(
                                currentPage = movieResults.page,
                                totalPages = movieResults.totalPages
                            )

                            (items.last() as? MovieGridContentViewModel)?.updateMovieItems(
                                newItemMovieListViewModel = itemMovieListViewModel,
                                watchlistMovies = watchlistMovies
                            ) ?: items.replaceAt(
                                items.lastIndex,
                                MovieGridContentViewModel(
                                    lifecycle = config.lifecycle,
                                    movieListType = action.movieListType,
                                    itemMovieListViewModel = itemMovieListViewModel
                                )
                            )
                        }
                    } ?: run {
                        withContext(Dispatchers.Main) {
                            items.replaceAt(
                                index = items.lastIndex,
                                item = when (movieResultResponse) {
                                    is DataResponse.Error -> errorViewModel
                                    is DataResponse.Empty -> GenericErrorViewModels.EmptyViewModel
                                    else -> LoadingViewModel
                                }
                            )
                        }
                    }
                }
        }
    }
}