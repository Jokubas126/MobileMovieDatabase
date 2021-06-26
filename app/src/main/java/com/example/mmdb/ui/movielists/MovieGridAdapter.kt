package com.example.mmdb.ui.movielists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.R
import com.example.mmdb.databinding.ItemMovieBinding
import com.jokubas.mmdb.model.data.entities.Movie

class MovieGridAdapter : RecyclerView.Adapter<MovieGridAdapter.ViewHolder>() {

    val movieList = mutableListOf<Movie>()

    fun updateMovieList(movieList: List<Movie>?) {
        this.movieList.clear()
        movieList?.let { this.movieList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
}