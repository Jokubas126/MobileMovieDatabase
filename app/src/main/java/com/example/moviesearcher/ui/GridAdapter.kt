package com.example.moviesearcher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearcher.R
import com.example.moviesearcher.databinding.ItemMovieBinding
import com.example.moviesearcher.model.data.Movie

class GridAdapter(
    private var customListBtnVisibility: Int,
    private var watchlistBtnVisibility: Int,
    private var deleteBtnVisibility: Int
): RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    val movieList = mutableListOf<Movie>()

    private var itemClickListener: ItemClickListener? = null
    private var personalListActionListener: PersonalListActionListener? = null

    interface ItemClickListener {
        fun onMovieClick(view: View, movie: Movie)
    }

    interface PersonalListActionListener {
        fun onPlaylistAdd(movie: Movie)
        fun onDeleteClicked(view: View, movie: Movie)
    }

    fun updateMovieList(movieList: List<Movie>?) {
        this.movieList.clear()
        if (movieList != null)
            this.movieList.addAll(movieList)
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

    fun setItemClickListener(listener: ItemClickListener){
        itemClickListener = listener
    }

    fun setPersonalListActionListener(listener: PersonalListActionListener){
        personalListActionListener = listener
    }

    inner class ViewHolder(itemView: ItemMovieBinding): RecyclerView.ViewHolder(itemView.root) {
        private val view = itemView

        fun onBind(movie: Movie){
            view.movie = movie
            view.root.setOnClickListener { itemClickListener?.onMovieClick(it, movie) }

            view.playlistAddBtn.visibility = customListBtnVisibility
            if (customListBtnVisibility != View.GONE)
                view.playlistAddBtn.setOnClickListener { personalListActionListener?.onPlaylistAdd(movie) }


            /*if(movie.isInWatchlist)
                view.watchlistBtn.setBackgroundResource(R.drawable.ic_star_full)
            else view.watchlistBtn.setBackgroundResource(R.drawable.ic_star_empty)

            view.watchlistBtn.visibility = View.GONE
            view.watchlistBtn.setOnClickListener {
                if(movie.isInWatchlist)
                    it.setBackgroundResource(R.drawable.ic_star_empty)
                else it.setBackgroundResource(R.drawable.ic_star_full)

            }*/

            view.deleteBtn.visibility = deleteBtnVisibility
            if (deleteBtnVisibility != View.GONE)
                view.deleteBtn.setOnClickListener { personalListActionListener?.onDeleteClicked(view.root, movie) }
        }
    }
}