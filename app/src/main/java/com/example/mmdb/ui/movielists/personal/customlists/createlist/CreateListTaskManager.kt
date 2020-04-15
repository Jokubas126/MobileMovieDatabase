package com.example.mmdb.ui.movielists.personal.customlists.createlist

import android.app.Application
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.room.repositories.MovieListRepository

class CreateListTaskManager(
    application: Application,
    popupWindow: CreateListPopupWindow
) : CreateListPopupWindow.ListAddedListener {

    private val movieListRepository = MovieListRepository(application)

    init {
        popupWindow.setListAddedListener(this)
    }

    override fun onListAdded(listName: String) {
        movieListRepository.insertOrUpdateMovieList(CustomMovieList(0, null, listName, null))
    }
}