package com.example.mmdb.ui.personal.customlists.createlist

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.popup_window_create_list.view.*

class CreateListPopupWindow(
    private val view: View,
    width: Int, height: Int,
    private val listener: ListAddedListener
) : PopupWindow(view, width, height, true) {

    interface ListAddedListener {
        fun onListAdded(listName: String)
    }

    init {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        showAtLocation(view, Gravity.CENTER, 0, 0)

        view.create_button.setOnClickListener {
            listener.onListAdded(view.list_title_edit_text.text.toString().trim())
            dismiss()
        }

        view.cancel_button.setOnClickListener { dismiss() }
        view.popup_window_outside.setOnClickListener{ dismiss() }
    }

}