package com.example.moviesearcher.ui.personal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.model.data.MovieList
import kotlinx.android.synthetic.main.item_personal_list.view.*

class PersonalListsAdapter(private val movieLists: List<MovieList>): RecyclerView.Adapter<PersonalListsAdapter.ViewHolder>() {

    fun updateMovieLists(newLists: List<MovieList>){
        (movieLists as MutableList).clear()
        movieLists.addAll(newLists)
        notifyDataSetChanged()
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onBind(movieList: MovieList){
            itemView.list_title.text = movieList.listTitle
        }
    }
}