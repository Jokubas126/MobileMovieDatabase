package com.example.moviesearcher.ui.personal.customlists

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.CustomMovieList
import com.example.moviesearcher.model.room.repositories.MovieListRepository
import com.example.moviesearcher.model.room.repositories.RoomMovieRepository
import com.example.moviesearcher.ui.popup_windows.CreateListPopupWindow

class CustomListsViewModel(application: Application) : AndroidViewModel(application),
    CreateListPopupWindow.ListAddedListener {

    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    lateinit var movieLists: LiveData<List<CustomMovieList>>
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private val movieListRepository = MovieListRepository(application)
    private val movieRepository = RoomMovieRepository(application)

    private lateinit var popupWindow: PopupWindow

    fun fetch() {
        _loading.value = true
        _error.value = false
        movieLists = movieListRepository.getAllMovieLists()
        _loading.value = false
    }

    fun showCreateListPopupWindow(context: Context) {
        popupWindow =
            CreateListPopupWindow(
                View.inflate(context, R.layout.popup_window_create_list, null),
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                this
            )
    }

    override fun onListAdded(listName: String) {
        movieListRepository.insertOrUpdateMovieList(CustomMovieList(0, null, listName, null))
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