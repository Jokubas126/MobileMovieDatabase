package com.example.mmdb.ui.movielists.personal.customlists

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.R
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.room.repositories.MovieListRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.ui.movielists.personal.customlists.createlist.CreateListPopupWindow
import com.example.mmdb.ui.movielists.personal.customlists.createlist.CreateListTaskManager

class CustomListsViewModel(application: Application) : AndroidViewModel(application) {

    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    var movieLists: LiveData<List<CustomMovieList>>
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private val movieListRepository = MovieListRepository(application)
    private val movieRepository = RoomMovieRepository(application)

    init {
        _loading.value = true
        _error.value = false
        movieLists = movieListRepository.getAllCustomMovieListLiveData()
        _loading.value = false
    }

    fun showCreateListPopupWindow(context: Context) {
        CreateListTaskManager(
            getApplication(),
            CreateListPopupWindow(
                View.inflate(context, R.layout.popup_window_create_list, null),
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun onListClicked(view: View, list: CustomMovieList) {
        val action =
            CustomListsFragmentDirections.movieGridFragment(list.roomId.toLong(), list.listTitle)
        Navigation.findNavController(view).navigate(action)
    }

    fun updateMovieList(list: CustomMovieList) {
        movieListRepository.insertOrUpdateMovieList(list)
    }

    fun deleteList(list: CustomMovieList) {
        val movieIdList = list.movieIdList
        if (!movieIdList.isNullOrEmpty())
            for (id in movieIdList)
                movieRepository.deleteMovieById(id)
        movieListRepository.deleteList(list)
    }
}