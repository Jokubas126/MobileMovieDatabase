package com.example.moviesearcher.ui.grids.searchgrid

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.example.moviesearcher.util.KEY_SEARCH_QUERY
import com.example.moviesearcher.util.showProgressSnackBar
import com.example.moviesearcher.util.showToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class SearchGridViewModel(application: Application) : AndroidViewModel(application), BaseGridViewModel,
    PersonalListsPopupWindow.ListsConfirmedClickListener {

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var page = 1
    private var isListFull = false
    private var searchQuery: String? = null

    override fun fetch(arguments: Bundle?) {
        _loading.value = true
        arguments?.let {
            searchQuery = arguments.getString(KEY_SEARCH_QUERY)
            getMovieList()
        }
    }

    override fun addData() {
        if (!isListFull) {
            _error.value = false
            _loading.value = true
            page++
            getMovieList()
        }
    }

    override fun refresh() {
        _error.value = false
        _loading.value = true
        page = 1
        isListFull = false
        clearMovies()
        getMovieList()
    }

    private fun getMovieList(){
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getSearchedMovies(searchQuery!!)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (page == response.body()!!.totalPages)
                        isListFull = true
                    getGenres(response.body()!!)
                } else {
                    _loading.value = false
                    _error.value = true
                }
            }
        }
    }

    private fun getGenres(movieList: MovieResults) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = MovieRepository().getGenreMap()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    movieList.formatGenres(response.body()!!)
                    _movies.value = movieList.results
                    _loading.value = false
                    _error.value = false
                } else {
                    _loading.value = false
                    _error.value = true
                }
            }
        }
    }

    private fun clearMovies() {
        _movies.value = ArrayList()
    }

    override fun onMovieClicked(view: View, movie: Movie) {
        val action = SearchGridFragmentDirections.actionMovieDetails()
        action.movieRemoteId = movie.remoteId
        Navigation.findNavController(view).navigate(action)
    }

    override fun onPlaylistAddCLicked(
        root: View,
        movie: Movie
    ) {
        val popupWindow = PersonalListsPopupWindow(
            root,
            View.inflate(root.context, R.layout.popup_window_personal_lists_to_add, null),
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            movie,
            this
        )
        val movieLists = MovieListDatabase.getInstance(root.context).movieListDao().getAllMovieLists()
        movieLists.observeForever {
            if (!it.isNullOrEmpty())
                popupWindow.setupLists(it)
        }
    }

    override fun onConfirmClicked(root: View, movie: Movie, checkedLists: List<LocalMovieList>): Boolean {
        if (checkedLists.isEmpty()) {
            showToast(getApplication(), getApplication<Application>().getString(R.string.select_a_list), Toast.LENGTH_SHORT)
            return false
        }
        CoroutineScope(Dispatchers.IO).launch {
            val fullMovie = MovieRepository().getMovieDetails(movie.remoteId).body()
            fullMovie?.let {
                showProgressSnackBar(root, getApplication<Application>().getString(R.string.being_uploaded_to_list))
                it.finalizeInitialization()
                val movieRoomId = PersonalMovieRepository(getApplication()).insertOrUpdateMovie(it)
                for (list in checkedLists)
                    PersonalMovieListRepository(getApplication()).addMovieToMovieList(list, movieRoomId.toInt())

                showSnackbarActionCheckLists(root)
            }
        }
        return true
    }

    private fun showSnackbarActionCheckLists(root: View){
        CoroutineScope(Dispatchers.Main).launch {
            Snackbar.make(root, getApplication<Application>().getString(R.string.successfully_uploaded_to_list), Snackbar.LENGTH_LONG)
                .setAction(getApplication<Application>().getString(R.string.action_check_lists)){
                    val action = SearchGridFragmentDirections.actionGlobalCustomListsFragment()
                    Navigation.findNavController(root).navigate(action)
                }.show()
        }
    }
}