package com.example.mmdb.ui.movielists.customlists

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
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.util.dateToString

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

        private val inputManager: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        fun onBind(movieList: CustomMovieList) {
            this.movieList = movieList
            itemView.setOnClickListener { listOnClickListener?.onListClicked(it, movieList) }
        }

        private fun inflateOptionsMenu(view: View) {
            popupMenu = PopupMenu(context, view)
            popupMenu.menuInflater.inflate(R.menu.custom_list_options, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            TODO("Not yet implemented")
        }
    }
}