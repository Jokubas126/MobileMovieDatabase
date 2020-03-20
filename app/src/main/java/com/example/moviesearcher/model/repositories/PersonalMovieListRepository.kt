package com.example.moviesearcher.model.repositories

import android.app.Application
import com.example.moviesearcher.model.data.LocalMovieList
import com.example.moviesearcher.model.room.dao.MovieListDao
import com.example.moviesearcher.model.room.database.MovieListDatabase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PersonalMovieListRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieListDao: MovieListDao? = MovieListDatabase.getInstance(application).movieListDao()

    fun insertOrUpdateMovieList(movieList: LocalMovieList) {
        launch { insertOrUpdateMovieListBG(movieList) }
    }

    private suspend fun insertOrUpdateMovieListBG(movieList: LocalMovieList) {
        withContext(Dispatchers.IO) { movieListDao?.insertOrUpdateMovieList(movieList) }
    }

    fun getMovieListById(id: Int) = movieListDao?.getMovieListById(id)

    fun getAllMovieLists() = movieListDao?.getAllMovieLists()


    fun deleteList(list: LocalMovieList) {
        launch { deleteListBG(list) }
    }

    private suspend fun deleteListBG(list: LocalMovieList) {
        withContext(Dispatchers.IO) { movieListDao?.deleteList(list) }
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