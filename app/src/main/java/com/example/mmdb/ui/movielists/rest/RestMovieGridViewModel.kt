package com.example.mmdb.ui.movielists.rest

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.ui.movielists.ItemMovieConfig
import com.example.mmdb.ui.movielists.ItemMovieViewModel
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.room.repositories.GenresRepository
import com.jokubas.mmdb.model.room.repositories.WatchlistRepository
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsTaskManager
import com.example.mmdb.ui.movielists.personal.customlists.addtolists.AddToListsPopupWindow
import com.example.mmdb.ui.movielists.toItemMovieViewModel
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.data.entities.MovieResults
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import com.jokubas.mmdb.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding

class RestMovieGridViewModel(
    application: Application,
    arguments: Bundle?,
    private val onMovieClicked: (movieId: Int) -> Unit
) : AndroidViewModel(application) {

    private val progressManager = RemoteListProgressManager()

    val error: LiveData<Boolean>
        get() = progressManager.error
    val loading: LiveData<Boolean>
        get() = progressManager.loading

    private val args = arguments?.let { RestMovieGridFragmentArgs.fromBundle(arguments) }

    // repositories
    private val remoteMovieRepository = RemoteMovieRepository()
    private val watchlistRepository = WatchlistRepository(application)
    private val genresRepository = GenresRepository(application)

    private lateinit var watchlistMovieIdList: MutableList<Int>

    val items = ObservableArrayList<ItemMovieViewModel>()
    val itemBinding: ItemBinding<ItemMovieViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.item_movie)

    init {
        viewModelScope.launch {
            progressManager.loading()
            watchlistMovieIdList = watchlistRepository.getAllMovieIds() as MutableList<Int>
            getResponse()
        }
    }

    fun refresh() {
        progressManager.refresh()
        getResponse()
    }

    fun addData() {
        if (!progressManager.isListFull) {
            progressManager.addingData()
            getResponse()
        }
    }

    private fun getResponse() {
        CoroutineScope(Dispatchers.IO).launch {
            if (isNetworkAvailable(getApplication())) {
                progressManager.checkPages()
                args?.let {
                    remoteMovieRepository.getMovieResults(
                        progressManager.currentPage,
                        args.movieGridType,
                        args.keyCategory,
                        args.startYear,
                        args.endYear,
                        args.genreId,
                        args.languageKey,
                        args.searchQuery
                    )?.let { getMovieList(it) }
                } ?: run { progressManager.error() }
            } else {
                progressManager.error()
                networkUnavailableNotification(
                    getApplication()
                )
            }
        }
    }

    private fun getMovieList(results: MovieResults) {
        progressManager.checkIfListFull(results.totalPages)
        val formattedMovieList = checkWatchlistMovies(formatGenres(results.movieList))
        insertMovieListToData(formattedMovieList)
    }

    private fun formatGenres(movieList: List<Movie>): List<Movie> {
        for (movie in movieList)
            movie.formatGenresString(genresRepository.getGenresByIdList(movie.genreIds))
        return movieList
    }

    private fun checkWatchlistMovies(movieList: List<Movie>): List<Movie> {
        movieList.forEach { movie ->
            if (watchlistMovieIdList.contains(movie.remoteId))
                movie.isInWatchlist = true
        }
        return movieList
    }

    private fun insertMovieListToData(movieList: List<Movie>) {
        viewModelScope.launch {
            when (movieList.isNotEmpty()) {
                true -> {
                    movieList.forEach { movie ->
                        items.add(
                            movie.toItemMovieViewModel(
                                ItemMovieConfig(
                                    onItemSelected = { onMovieClicked(movie.remoteId) },
                                    onCustomListSelected = { onPlaylistAddCLicked(movie) },
                                    onWatchlistSelected = {
                                        movie.isInWatchlist = !movie.isInWatchlist
                                        updateWatchlist(movie)
                                    }
                                )
                            )
                        )
                    }
                    progressManager.success()
                }
                else -> progressManager.error()
            }
        }
    }

//------------------------ Watchlist ----------------------------//

    private fun updateWatchlist(movie: Movie) {
        if (movie.isInWatchlist) {
            watchlistRepository.insertOrUpdateMovie(WatchlistMovie(movie.remoteId))
            watchlistMovieIdList.add(movie.remoteId)
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.added_to_watchlist),
                Toast.LENGTH_SHORT
            )
        } else {
            watchlistRepository.deleteWatchlistMovie(movie.remoteId)
            watchlistMovieIdList.remove(movie.remoteId)
            showToast(
                getApplication(),
                getApplication<Application>().getString(R.string.deleted_from_watchlist),
                Toast.LENGTH_SHORT
            )
        }
    }

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
    }
}