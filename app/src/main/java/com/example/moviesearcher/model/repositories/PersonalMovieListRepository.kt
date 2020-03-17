package com.example.moviesearcher.model.repositories

import android.app.Application
import com.example.moviesearcher.model.data.MovieList
import com.example.moviesearcher.model.room.dao.MovieListDao
import com.example.moviesearcher.model.room.database.MovieListDatabase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PersonalMovieListRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var movieListDao: MovieListDao?

    init {
        val db = MovieListDatabase.getInstance(application)
        movieListDao = db.movieListDao()
    }

    fun insertOrUpdateMovieList(movieList: MovieList) {
        launch { insertOrUpdateMovieListBG(movieList) }
    }

    private suspend fun insertOrUpdateMovieListBG(movieList: MovieList) {
        withContext(Dispatchers.IO) {
            movieListDao?.insertOrUpdateMovieList(movieList)
        }
    }

    fun getMovieListById(id: Int) = movieListDao?.getMovieListById(id)

    fun getAllMovieLists() = movieListDao?.getAllMovieLists()


    fun deleteList(list: MovieList) {
        launch { deleteListBG(list) }
    }

    private suspend fun deleteListBG(list: MovieList) {
        withContext(Dispatchers.IO) {
            movieListDao?.deleteList(list)
        }
    }

    fun deleteAllList() {
        launch { deleteAllListsBG() }
    }

    private suspend fun deleteAllListsBG(){
        withContext(Dispatchers.IO){
            movieListDao?.deleteAllLists()
        }
    }
}