package com.example.mmdb.ui.movielists.moviegrid

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.movielists.discover.DiscoverSelectionViewModel
import com.example.mmdb.ui.movielists.pageselection.PageSelectionListViewModel
import com.jokubas.mmdb.feedback_ui.LoadingViewModel
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
        if (action.movieListType is MovieListType.Remote.Discover) {
            DiscoverSelectionViewModel(action.movieListType.discoverNameList)
        } else null

    private val pageSelectionListViewModel =
        PageSelectionListViewModel(viewModelScope + Dispatchers.IO)

    val contentItems: ObservableList<Any> = ObservableArrayList<Any>().apply {
        discoverSelectionViewModel?.let { add(it) }
        add(pageSelectionListViewModel)
        add(LoadingViewModel)
    }

    val contentItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(LoadingViewModel::class.java, BR.viewModel, R.layout.loading_view)
        .map(MovieGridContentViewModel::class.java, BR.viewModel, R.layout.movie_grid_content)
        .map(PageSelectionListViewModel::class.java, BR.viewModel, R.layout.page_selection_list)
        .map(DiscoverSelectionViewModel::class.java, BR.viewModel, R.layout.discover_selection_view)

    val refreshEventListener = {
        loadMovieList()
    }

    init {
        loadMovieList()
    }

    fun loadMovieList() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            //progressManager.error()
            Log.e("MovieGridViewModel", "loadMovieList: ", throwable)
        }) {

            combine(
                config.provideMovies.invoke(
                    action.movieListType,
                    pageSelectionListViewModel.currentPage
                ),
                config.provideWatchlist.invoke()
            ) { movies, watchlistMovies ->
                Pair(movies, watchlistMovies)
            }.collect { (movieResultResponse, watchlistMovies) ->
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

                        (contentItems.last() as? MovieGridContentViewModel)?.updateMovieItems(
                            newItemMovieListViewModel = itemMovieListViewModel,
                            watchlistMovies = watchlistMovies
                        ) ?: contentItems.replaceAt(
                                contentItems.lastIndex,
                                MovieGridContentViewModel(
                                    lifecycle = config.lifecycle,
                                    movieListType = action.movieListType,
                                    itemMovieListViewModel = itemMovieListViewModel
                                )
                            )
                    }
                } ?: run {
                    withContext(Dispatchers.Main) {
                        contentItems.replaceAt(contentItems.lastIndex, LoadingViewModel)
                    }
                    /*when (movieListData.movieResultDataResponse) {
                        is DataResponse.Error,
                        is DataResponse.Empty -> progressManager.error()
                        else -> //progressManager.loading()
                    }*/
                }
            }
        }
    }
}