package com.example.mmdb.ui.movielists.personal.customlists

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.*
import com.example.mmdb.R
import com.jokubas.mmdb.model.room.repositories.CustomMovieListRepository
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.ui.movielists.personal.customlists.createlist.CreateListPopupWindow
import com.example.mmdb.ui.movielists.personal.customlists.createlist.CreateListTaskManager
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomListsViewModel(application: Application) : AndroidViewModel(application) {

    private val _movieLists =
        CustomMovieListRepository(application).getAllCustomMovieListFlow()
        .asLiveData(CoroutineScope(Dispatchers.IO).coroutineContext)

    val movieLists: LiveData<List<CustomMovieList>>
        get() = _movieLists

    private val movieListRepository = CustomMovieListRepository(application)
    private val movieRepository = RoomMovieRepository(application)

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
        /*val action =
            CustomListsFragmentDirections.movieGridFragment(list.roomId.toLong(), list.listTitle)
        Navigation.findNavController(view).navigate(action)*/
    }

    fun updateMovieList(list: CustomMovieList) {
        CoroutineScope(Dispatchers.IO).launch {
            movieListRepository.insertOrUpdateMovieList(list)
        }
    }

    fun deleteList(list: CustomMovieList) {
        val movieIdList = list.movieIdList
        if (!movieIdList.isNullOrEmpty())
            for (id in movieIdList)
                movieRepository.deleteMovieById(id)
        movieListRepository.deleteList(list)
    }
}