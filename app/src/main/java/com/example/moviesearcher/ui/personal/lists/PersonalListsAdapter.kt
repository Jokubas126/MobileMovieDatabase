package com.example.moviesearcher.ui.personal.lists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.LocalMovieList
import kotlinx.android.synthetic.main.item_personal_list.view.*

class PersonalListsAdapter(private val movieLists: List<LocalMovieList>, private val listener: ListOnClickListener): RecyclerView.Adapter<PersonalListsAdapter.ViewHolder>() {

    fun updateMovieLists(newLists: List<LocalMovieList>){
        (movieLists as MutableList).clear()
        movieLists.addAll(newLists)
        notifyDataSetChanged()
    }

    interface ListOnClickListener{
        fun onListClicked(view: View, list: LocalMovieList)
        fun onDeleteClicked(list: LocalMovieList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_personal_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(movieLists[position], listener)
    }

    override fun getItemCount(): Int {
        return movieLists.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onBind(movieList: LocalMovieList, listener: ListOnClickListener){
            itemView.list_title.text = movieList.listTitle
            itemView.setOnClickListener { listener.onListClicked(it, movieList) }
            itemView.delete_button.setOnClickListener{ listener.onDeleteClicked(movieList) }
        }
    }
}