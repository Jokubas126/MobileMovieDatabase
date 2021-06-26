package com.example.mmdb.ui.movielists.customlists.createlist

import android.app.Application
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.room.repositories.CustomMovieListRepository
import com.jokubas.mmdb.util.DEFAULT_ID_VALUE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateListTaskManager(
    application: Application,
    popupWindow: CreateListPopupWindow
) : CreateListPopupWindow.ListAddedListener {

    private val movieListRepository =
        CustomMovieListRepository(application)

    init {
        popupWindow.setListAddedListener(this)
    }

    override fun onListAdded(listName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            movieListRepository.insertOrUpdateMovieList(
                CustomMovieList(
                    DEFAULT_ID_VALUE,
                    null,
                    listName,
                    null
                )
            )
        }
    }
}