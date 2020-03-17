package com.example.moviesearcher.ui.personal

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.MovieList
import com.example.moviesearcher.model.repositories.PersonalMovieListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalListsViewModel(application: Application) : AndroidViewModel(application),
    CreateListPopupWindow.ListAddedListener {

    //private var _movieLists = MutableLiveData<List<MovieList>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    lateinit var movieLists:  LiveData<List<MovieList>>
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private val movieListRepository = PersonalMovieListRepository(application)

    private lateinit var popupWindow: PopupWindow

    fun fetch() {
        _loading.value = true

        movieLists = movieListRepository.getAllMovieLists()!!
        _loading.value = false
        _error.value = false
    }

    fun showCreateListPopupWindow(context: Context) {
        popupWindow = CreateListPopupWindow(
            View.inflate(context, R.layout.popup_window_create_list, null),
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT,
            this
        )
    }

    override fun onListAdded(listName: String) {
        val list =
            if (!movieLists.value.isNullOrEmpty())
                movieLists.value
            else mutableListOf()

        val movieList = MovieList(0, listName, listOf())

        movieListRepository.insertOrUpdateMovieList(movieList)
        movieLists = movieListRepository.getAllMovieLists()!!
        //(list as MutableList).add(movieList)
        //_movieLists.value = list
    }
}