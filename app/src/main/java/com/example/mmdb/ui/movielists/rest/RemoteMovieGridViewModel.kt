package com.example.mmdb.ui.movielists.rest

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.config.AppConfig
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction
import com.example.mmdb.ui.movielists.ItemMovieConfig
import com.example.mmdb.ui.movielists.ItemMovieViewModel
import com.jokubas.mmdb.model.room.repositories.GenresRepository
import com.jokubas.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.ui.movielists.toItemMovieViewModel
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.constants.KEY_NOW_PLAYING
import com.jokubas.mmdb.util.constants.KEY_POPULAR
import com.jokubas.mmdb.util.constants.KEY_TOP_RATED
import com.jokubas.mmdb.util.constants.KEY_UPCOMING
import com.jokubas.mmdb.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class RemoteMovieGridViewModel(
    application: Application,
    private val appConfig: AppConfig,
    private val action: RemoteMovieGridFragmentAction,
    private val config: RemoteMovieGridFragmentConfig,
    private val onMovieClicked: (movieId: Int) -> Unit
) : AndroidViewModel(application) {

    private val progressManager = RemoteListProgressManager()

    val error: LiveData<Boolean>
        get() = progressManager.error
    val loading: LiveData<Boolean>
        get() = progressManager.loading

    // repositories
    private val remoteMovieRepository = appConfig.movieConfig.remoteMovieRepository
    private val watchlistRepository = WatchlistRepository(application)
    private val genresRepository = GenresRepository(application)

    private var watchlistMovieIdList = watchlistRepository.getAllMovieIds().asLiveData().apply {
        observeForever {
            refresh()
        }
    }

    val pageSelectionListViewModel = ObservableField<PageSelectionListViewModel>()
    val discoverSelectionViewModel: DiscoverSelectionViewModel? =
        if (action.movieListType is MovieListType.Discover) {
            DiscoverSelectionViewModel(action.movieListType.discoverNameList)
        } else null


    val itemsMovie = ObservableArrayList<ItemMovieViewModel>()
    val itemMoviesBinding: ItemBinding<ItemMovieViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_movie)

    init {
        progressManager.loading()
        getResponse(progressManager.currentPage)
    }

    fun refresh() {
        progressManager.load()
        getResponse(progressManager.currentPage)
    }

    private fun fetchData(page: Int) {
        getResponse(page)
    }

    private fun getResponse(page: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (appConfig.networkCheckConfig.isNetworkAvailable()) {
                val movieResults = when (action.movieListType) {
                    is MovieListType.Popular -> remoteMovieRepository.getTypeMovies(
                        KEY_POPULAR,
                        page
                    )
                    is MovieListType.TopRated -> remoteMovieRepository.getTypeMovies(
                        KEY_TOP_RATED,
                        page
                    )
                    is MovieListType.NowPlaying -> remoteMovieRepository.getTypeMovies(
                        KEY_NOW_PLAYING,
                        page
                    )
                    is MovieListType.Upcoming -> remoteMovieRepository.getTypeMovies(
                        KEY_UPCOMING,
                        page
                    )
                    is MovieListType.Discover -> remoteMovieRepository.getDiscoveredMovies(
                        page = page,
                        startYear = action.movieListType.startYear,
                        endYear = action.movieListType.endYear,
                        genreKeys = action.movieListType.genreKeys.toTypedArray(),
                        languageKeys = action.movieListType.languageKeys.toTypedArray()
                    )
                    is MovieListType.Search -> remoteMovieRepository.getSearchedMovies(
                        page = page,
                        query = action.movieListType.searchQuery ?: ""
                    )
                    else -> null
                }
                movieResults?.let { configureResults(it) } ?: progressManager.error()
                /*args?.apply {
                    remoteMovieRepository.getMovieResults(
                        page,
                        movieGridType,
                        keyCategory,
                        startYear,
                        endYear,
                        genreKeys,
                        languageKeys,
                        searchQuery
                    )?.let {  }
                } ?: progressManager.error()*/
            } else {
                progressManager.error()
                appConfig.networkCheckConfig.networkUnavailableNotification()
            }
        }
    }

    private fun configureResults(results: MovieResults) {
        results.apply {
            progressManager.currentPage = page
            progressManager.checkIfListFull(totalPages)

            for (movie in movieList)
                movie.formatGenresString(genresRepository.getGenresByIdList(movie.genreIds))

            movieList.forEach { movie ->
                movie.isInWatchlist = watchlistMovieIdList.value?.contains(movie.remoteId) == true
            }
        }
        insertMovieListToData(results)
    }

    private fun insertMovieListToData(results: MovieResults) {
        when (results.movieList.isNotEmpty()) {
            true -> {
                viewModelScope.launch {
                    pageSelectionListViewModel.set(
                        PageSelectionListViewModel(results.totalPages, results.page)
                        { pageNumber ->
                            fetchData(pageNumber)
                        }
                    )

                    itemsMovie.removeAll { true }

                    results.movieList.forEach { movie ->
                        itemsMovie.add(
                            movie.toItemMovieViewModel(
                                getItemMovieConfig(movie, results.page, itemsMovie.size)
                            )
                        )
                    }
                    progressManager.success()
                }
            }
            else -> progressManager.error()
        }
    }

    private fun getItemMovieConfig(movie: Movie, page: Int, position: Int) =
        ItemMovieConfig(
            position = position,
            page = page,
            onItemSelected = {
                onMovieClicked(movie.remoteId)
            },
            onCustomListSelected = {
                Toast.makeText(getApplication(), "Playlist clicked", Toast.LENGTH_SHORT).show()
                //onPlaylistAddCLicked(movie) // TODO implement this
            },
            onWatchlistSelected = {
                updateWatchlist(movie)
            }
        )

//------------------------ Watchlist ----------------------------//

    private fun updateWatchlist(movie: Movie) {
        if (movie.isInWatchlist) {
            watchlistRepository.deleteWatchlistMovie(movie.remoteId)
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.deleted_from_watchlist),
                Toast.LENGTH_SHORT
            )
        } else {
            watchlistRepository.insertOrUpdateMovie(WatchlistMovie(movie.remoteId))
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.added_to_watchlist),
                Toast.LENGTH_SHORT
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        watchlistMovieIdList.removeObserver {}
    }
/*
//------------------ Custom lists --------------------------//

    private fun onPlaylistAddCLicked(movie: Movie) {
        AddToListsTaskManager(
            getApplication(),
            AddToListsPopupWindow(
                View.inflate(getApplication(), R.layout.popup_window_personal_lists_to_add, null),
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                movie
            )
        )
    }*/
}