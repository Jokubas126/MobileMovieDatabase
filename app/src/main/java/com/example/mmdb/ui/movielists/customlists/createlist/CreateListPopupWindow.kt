package com.example.mmdb.ui.movielists.customlists.createlist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow

class CreateListPopupWindow(
    private val view: View,
    width: Int, height: Int
) : PopupWindow(view, width, height, true) {

    private var listAddedListener: ListAddedListener? = null

    init {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    interface ListAddedListener {
        fun onListAdded(listName: String)
    }

    fun setListAddedListener(listener: ListAddedListener) {
        listAddedListener = listener
    }
}