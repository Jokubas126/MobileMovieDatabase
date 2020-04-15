package com.example.mmdb.ui.movielists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.R
import com.example.mmdb.databinding.ItemMovieBinding
import com.example.mmdb.model.data.Movie

class MovieGridAdapter(
    private var customListBtnVisibility: Int,
    private var watchlistBtnVisibility: Int,
    private var deleteBtnVisibility: Int
) : RecyclerView.Adapter<MovieGridAdapter.ViewHolder>() {

    val movieList = mutableListOf<Movie>()

    private var itemClickListener: ItemClickListener? = null
    private var watchlistActionListener: WatchlistActionListener? = null
    private var personalListActionListener: PersonalListActionListener? = null
    private var personalListDeleteListener: PersonalListDeleteListener? = null

    interface ItemClickListener {
        fun onMovieClick(view: View, movie: Movie)
    }

    interface WatchlistActionListener {
        fun onWatchlistCheckChanged(movie: Movie)
    }

    interface PersonalListActionListener {

        fun onPlaylistAdd(movie: Movie)
    }

    interface PersonalListDeleteListener {
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
        val view: ItemMovieBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(movieList[position])
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

    fun setWatchlistActionListener(listener: WatchlistActionListener) {
        watchlistActionListener = listener
    }

    fun setPersonalListActionListener(listener: PersonalListActionListener) {
        personalListActionListener = listener
    }

    fun setPersonalListDeleteListener(listener: PersonalListDeleteListener) {
        personalListDeleteListener = listener
    }

    inner class ViewHolder(itemView: ItemMovieBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val view = itemView
        private lateinit var movie: Movie

        fun onBind(movie: Movie) {
            view.movie = movie
            this.movie = movie
            view.root.setOnClickListener { itemClickListener?.onMovieClick(it, movie) }

            configurePlaylistAdd()
            configureWatchlist()
            configureDeleteMovie()
        }

        private fun configureWatchlist(){
            if (movie.isInWatchlist)
                view.watchlistBtn.setBackgroundResource(R.drawable.ic_star_full)
            else
                view.watchlistBtn.setBackgroundResource(R.drawable.ic_star_empty)
            view.watchlistBtn.visibility = watchlistBtnVisibility

            view.watchlistBtn.setOnClickListener {
                movie.isInWatchlist = !movie.isInWatchlist
                watchlistActionListener?.onWatchlistCheckChanged(movie)
                if (movie.isInWatchlist)
                    it.setBackgroundResource(R.drawable.ic_star_full)
                else it.setBackgroundResource(R.drawable.ic_star_empty)
            }
        }

        private fun configurePlaylistAdd(){
            view.playlistAddBtn.visibility = customListBtnVisibility
            if (customListBtnVisibility != View.GONE)
                view.playlistAddBtn.setOnClickListener {
                    personalListActionListener?.onPlaylistAdd(movie)
                }
        }

        private fun configureDeleteMovie(){
            view.deleteBtn.visibility = deleteBtnVisibility
            if (deleteBtnVisibility != View.GONE)
                view.deleteBtn.setOnClickListener {
                    personalListDeleteListener?.onDeleteClicked(it, movie)
                }
        }
    }
}