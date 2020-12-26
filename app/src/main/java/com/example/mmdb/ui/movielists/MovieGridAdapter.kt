package com.example.mmdb.ui.movielists

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mmdb.R
import com.example.mmdb.databinding.ItemMovieBinding
import com.jokubas.mmdb.model.data.entities.Movie

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
        fun onDeleteClicked(view: View, movie: Movie, position: Int)
    }

    fun updateMovieList(movieList: List<Movie>?) {
        this.movieList.clear()
        movieList?.let { this.movieList.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    /*fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }*/


    inner class ViewHolder(binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        private val view = binding
        private lateinit var viewModel: ItemMovieViewModel

        /*fun onBind(viewModel: ItemMovieViewModel) {
            view.viewModel = viewModel
            view.root.setOnClickListener { itemClickListener?.onMovieClick(it, viewModel.movie) }

            *//*configurePlaylistAdd()
            configureWatchlist()
            configureDeleteMovie()*//*
        }*/

        private fun configureWatchlist() {
            /*if (viewModel.movie.isInWatchlist)
                view.watchlistBtn.setBackgroundResource(R.drawable.ic_star_full)
            else
                view.watchlistBtn.setBackgroundResource(R.drawable.ic_star_empty)*/
            //view.watchlistBtn.visibility = watchlistBtnVisibility

            /*view.watchlistBtn.setOnClickListener {
                viewModel.movie.isInWatchlist = !viewModel.movie.isInWatchlist
                watchlistActionListener?.onWatchlistCheckChanged(viewModel.movie)
                if (viewModel.movie.isInWatchlist)
                    it.setBackgroundResource(R.drawable.ic_star_full)
                else it.setBackgroundResource(R.drawable.ic_star_empty)
            }*/
        }

        /*private fun configurePlaylistAdd() {
            view.playlistAddBtn.visibility = customListBtnVisibility
            if (customListBtnVisibility != View.GONE)
                view.playlistAddBtn.setOnClickListener {
                    personalListActionListener?.onPlaylistAdd(viewModel.movie)
                }
        }

        private fun configureDeleteMovie() {
            view.deleteBtn.visibility = deleteBtnVisibility
            if (deleteBtnVisibility != View.GONE)
                view.deleteBtn.setOnClickListener {
                    personalListDeleteListener?.onDeleteClicked(it, viewModel.movie, adapterPosition)
                }
        }*/
    }
}