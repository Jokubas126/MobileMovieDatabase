package com.example.moviesearcher.ui.grids

import android.os.Bundle
import android.view.View
import com.example.moviesearcher.model.data.Movie

interface BaseGridViewModel{
    fun fetch(arguments: Bundle?)
    fun addData()
    fun refresh()
    fun onMovieClicked(view: View, movie: Movie)
    fun onPlaylistAddCLicked(root: View, movie: Movie)
}
