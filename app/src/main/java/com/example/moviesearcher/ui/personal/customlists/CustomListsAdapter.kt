package com.example.moviesearcher.ui.personal.customlists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.LocalMovieList
import kotlinx.android.synthetic.main.item_personal_list.view.*

class CustomListsAdapter: RecyclerView.Adapter<CustomListsAdapter.ViewHolder>() {

    val movieLists =  mutableListOf<LocalMovieList>()

    private var listOnClickListener: ListOnClickListener? = null

    fun updateMovieLists(lists: List<LocalMovieList>){
        movieLists.clear()
        movieLists.addAll(lists)
        notifyDataSetChanged()
    }

    interface ListOnClickListener{
        fun onListClicked(view: View, list: LocalMovieList)
        fun onDeleteClicked(view: View, list: LocalMovieList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_personal_list, parent, false))
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onBind(movieList: LocalMovieList){
            itemView.list_title.text = movieList.listTitle
            itemView.setOnClickListener { listOnClickListener?.onListClicked(it, movieList) }
            itemView.delete_button.setOnClickListener{ listOnClickListener?.onDeleteClicked(it, movieList) }
        }
    }
}