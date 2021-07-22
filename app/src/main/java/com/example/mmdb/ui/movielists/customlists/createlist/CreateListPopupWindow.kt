package com.example.mmdb.ui.movielists.customlists.createlist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.popup_window_create_list.view.*

class CreateListPopupWindow(
    private val view: View,
    width: Int, height: Int
) : PopupWindow(view, width, height, true) {

    private var listAddedListener: ListAddedListener? = null

    init {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        showAtLocation(view, Gravity.CENTER, 0, 0)

        view.create_button.setOnClickListener {
            listAddedListener?.onListAdded(view.list_title_edit_text.text.toString().trim())
            dismiss()
        }

        view.cancel_button.setOnClickListener { dismiss() }
        view.popup_window_outside.setOnClickListener{ dismiss() }
    }

    interface ListAddedListener {
        fun onListAdded(listName: String)
    }

    fun setListAddedListener(listener: ListAddedListener) {
        listAddedListener = listener
    }
}