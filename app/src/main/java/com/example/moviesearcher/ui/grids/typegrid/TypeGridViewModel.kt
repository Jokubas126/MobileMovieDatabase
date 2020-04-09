package com.example.moviesearcher.ui.grids.typegrid

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.MovieResults
import com.example.moviesearcher.model.repositories.MovieRepository
import com.example.moviesearcher.model.repositories.PersonalMovieListRepository
import com.example.moviesearcher.model.repositories.PersonalMovieRepository
import com.example.moviesearcher.model.room.database.MovieListDatabase
import com.example.moviesearcher.ui.grids.BaseGridViewModel
import com.example.moviesearcher.ui.popup_windows.PersonalListsPopupWindow
import com.example.moviesearcher.util.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TypeGridViewModel(application: Application) : AndroidViewModel(application),
    BaseGridViewModel,
    PersonalListsPopupWindow.ListsConfirmedClickListener {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var currentPage = 1
    private var fetchedPage = 1
    private var isListFull = false
    private lateinit var movieListType: String

    override fun fetch(arguments: Bundle?) {
        _error.value = false
        _loading.value = true
        isListFull = false

        arguments?.let {
            val args = TypeGridFragmentArgs.fromBundle(it)
            movieListType =
                if (!args.keyCategory.isBlank())
                    args.keyCategory
                else KEY_POPULAR
            getMovieList()
        }
    }

    override fun refresh() {
        isListFull = false
        _loading.value = true
        currentPage = 1
        getMovieList()
    }

    override fun addData() {
        if (!isListFull) {
            _loading.value = true
            currentPage++
            getMovieList()
        }
    }

    private fun getMovieList() {
        if (isNetworkAvailable(getApplication())) {
            configurePages()
            CoroutineScope(Dispatchers.IO).launch {
                val response = MovieRepository().getMovies(movieListType, currentPage)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        if (currentPage == response.body()!!.totalPages)
                            isListFull = true
                        getGenres(response.body()!!)
                    } else {
                        _loading.value = false
                        _error.value = true
                    }
                }
            }
        } else {
            _loading.value = false
            networkUnavailableNotification(getApplication())
        }
    }

    private fun getGenres(movieList: MovieResults) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getGenreMap()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.formatGenres(response.body()!!)
                    val tmpMovieList: MutableList<Movie> = _movies.value as MutableList
                    tmpMovieList.addAll(movieList.results)
                    _movies.value = tmpMovieList
                    fetchedPage = currentPage
                    _loading.value = false
                    _error.value = false
                } else {
                    _loading.value = false
                    _error.value = true
                }
            }
        }
    }

    private fun configurePages() {
        if (currentPage == 1) {
            fetchedPage = 1
            _movies.value = mutableListOf()
        }
    }

    override fun onMovieClicked(view: View, movie: Movie) {
        val action = TypeGridFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }

    override fun onPlaylistAddCLicked(root: View, movie: Movie) {
        val popupWindow = PersonalListsPopupWindow(
            root,
            View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            movie,
            this
        )
        val movieLists =
            MovieListDatabase.getInstance(root.context).movieListDao().getAllMovieLists()
        movieLists.observeForever {
            if (!it.isNullOrEmpty())
                popupWindow.setupLists(it)
        }
    }

    override fun onConfirmClicked(root: View, movie: Movie, checkedLists: List<LocalMovieList>): Boolean {
        return when {
            checkedLists.isEmpty() -> {
                showToast(
                    getApplication(),
                    getApplication<Application>().getString(R.string.select_a_list),
                    Toast.LENGTH_SHORT
                )
                false
            }
            isNetworkAvailable(getApplication()) -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val fullMovie = MovieRepository().getMovieDetails(movie.remoteId).body()
                    fullMovie?.let {
                        showProgressSnackBar(
                            root,
                            getApplication<Application>().getString(R.string.being_uploaded_to_list)
                        )
                        it.finalizeInitialization(getApplication())
                        val movieRoomId = PersonalMovieRepository(getApplication()).insertOrUpdateMovie(getApplication(), it)
                        for (list in checkedLists)
                            PersonalMovieListRepository(getApplication()).addMovieToMovieList(
                                list,
                                movieRoomId.toInt()
                            )
                        showSnackbarActionCheckLists(root)
                    }
                }
                true
            }
            else -> {
                networkUnavailableNotification(getApplication())
                false
            }
        }
    }

    private fun showSnackbarActionCheckLists(root: View) {
        CoroutineScope(Dispatchers.Main).launch {
            Snackbar.make(
                    root,
                    getApplication<Application>().getString(R.string.successfully_uploaded_to_list),
                    Snackbar.LENGTH_LONG
                )
                .setAction(getApplication<Application>().getString(R.string.action_check_lists)) {
                    val action = TypeGridFragmentDirections.actionGlobalCustomListsFragment()
                    Navigation.findNavController(root).navigate(action)
                }.show()
        }
    }
}