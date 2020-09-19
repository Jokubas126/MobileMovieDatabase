package com.example.mmdb.ui.movielists.personal.customlists

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.R
import com.jokubas.mmdb.model.data.dataclasses.CustomMovieList
import com.jokubas.mmdb.util.dateToString
import kotlinx.android.synthetic.main.item_custom_list.view.*

class CustomListsAdapter(val context: Context) :
    RecyclerView.Adapter<CustomListsAdapter.ViewHolder>() {

    val movieLists = mutableListOf<CustomMovieList>()

    private var listOnClickListener: ListOnClickListener? = null
    private var listOptionsOnClickListener: ListOptionsListener? = null

    fun updateMovieLists(lists: List<CustomMovieList>) {
        movieLists.clear()
        movieLists.addAll(lists)
        notifyDataSetChanged()
    }

    interface ListOnClickListener {
        fun onListClicked(view: View, list: CustomMovieList)
    }

    interface ListOptionsListener {
        fun onEditListTitle(list: CustomMovieList)
        fun onDeleteClicked(view: View, list: CustomMovieList, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(movieLists[position])
    }

    override fun getItemCount(): Int {
        return movieLists.size
    }

    fun setListOnClickListener(listener: ListOnClickListener) {
        listOnClickListener = listener
    }

    fun setListOptionsListener(listener: ListOptionsListener) {
        listOptionsOnClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        PopupMenu.OnMenuItemClickListener {

        private lateinit var popupMenu: PopupMenu

        private lateinit var movieList: CustomMovieList
        private val title = itemView.list_title

        private val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        fun onBind(movieList: CustomMovieList) {
            this.movieList = movieList
            setupTitle()
            setupUpdateDate()
            inflateOptionsMenu(itemView.options_menu_btn)
            itemView.setOnClickListener { listOnClickListener?.onListClicked(it, movieList) }
            itemView.options_menu_btn.setOnClickListener { popupMenu.show() }
        }

        private fun inflateOptionsMenu(view: View) {
            popupMenu = PopupMenu(context, view)
            popupMenu.menuInflater.inflate(R.menu.custom_list_options, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
        }

        private fun setupTitle() {
            title.setText(movieList.listTitle)
            title.setOnEditorActionListener { titleView, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_NEXT -> {
                        title.isFocusableInTouchMode = false
                        title.clearFocus()
                        movieList.listTitle = titleView.text.toString()
                        listOptionsOnClickListener?.onEditListTitle(movieList)
                        true
                    }
                    else -> false
                }
            }
        }

        private fun setupUpdateDate() {
            movieList.updateDate?.let {
                itemView.update_date.text = dateToString(it)
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.edit -> {
                    title.isFocusableInTouchMode = true
                    title.requestFocus()
                    title.setSelection(title.text.length)
                    title.postDelayed({
                        inputManager.showSoftInput(title, InputMethodManager.SHOW_IMPLICIT)
                    }, 100)
                    return true
                }
                R.id.delete -> {
                    listOptionsOnClickListener?.onDeleteClicked(itemView, movieList, adapterPosition)
                    return true
                }
            }
            return false
        }
    }
}