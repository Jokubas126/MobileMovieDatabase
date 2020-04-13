package com.example.moviesearcher.ui.remotegrids

import android.view.View
import com.example.moviesearcher.model.data.Movie

interface BaseGridViewModel{
    fun addData()
    fun refresh()
    fun onMovieClicked(view: View, movie: Movie)
    fun onPlaylistAddCLicked(movie: Movie, root: View)
}
