package com.example.mmdb.ui.movielists.customlists.addtolists

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.data.entities.Movie

class AddToListsPopupWindow(
    private val view: View,
    width: Int, height: Int,
    private val selectedMovie: Movie
) : PopupWindow(view, width, height, true),
    AddToListsAdapter.ListCheckedListener {

    private var listsConfirmedClickListener: ListsConfirmedClickListener? = null

    private val checkedLists = mutableListOf<CustomMovieList>()

    init {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    interface ListsConfirmedClickListener {
        fun onConfirmListsClicked(movie: Movie, checkedLists: List<CustomMovieList>): Boolean
    }

    fun setListsConfirmedClickListener(listener: ListsConfirmedClickListener) {
        listsConfirmedClickListener = listener
    }

    override fun onListChecked(movieList: CustomMovieList, isChecked: Boolean) {
        if (isChecked) checkedLists.add(movieList)
        else checkedLists.remove(movieList)
    }
}