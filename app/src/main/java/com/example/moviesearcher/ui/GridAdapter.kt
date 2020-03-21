package com.example.moviesearcher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.databinding.ItemMovieBinding
import com.example.moviesearcher.model.data.Movie

class GridAdapter(private val listener: AdapterItemClickListener): RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private val movieList: MutableList<Movie> = mutableListOf()

    interface AdapterItemClickListener {
        fun onMovieClicked(view: View, movie: Movie)
        fun onPlaylistAddListener(movie: Movie)
    }

    fun updateMovieList(movieList: List<Movie>?) {
        if (movieList.isNullOrEmpty())
            this.movieList.clear()
        else {
            if (this.movieList.containsAll(movieList))
                return
            this.movieList.addAll(movieList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemMovieBinding = DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class ViewHolder(itemView: ItemMovieBinding): RecyclerView.ViewHolder(itemView.root) {
        private val view = itemView

        fun onBind(movie: Movie){
            view.movie = movie
            view.root.setOnClickListener { listener.onMovieClicked(it, movie) }

            view.playlistAddBtn.setOnClickListener { listener.onPlaylistAddListener(movie) }
        }
    }
}