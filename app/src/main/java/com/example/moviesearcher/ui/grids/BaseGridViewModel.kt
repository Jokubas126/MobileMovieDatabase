package com.example.moviesearcher.ui.grids

import android.os.Bundle
import android.view.View


interface BaseGridViewModel{
    fun fetch(args: Bundle?)
    fun addData()
    fun refresh()
    fun onMovieClicked(view: View, movieId: Int)
}
