package com.example.mmdb.model.room.repositories

import android.app.Application
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.room.databases.MovieListDatabase
import com.example.mmdb.util.getCurrentDate
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CustomMovieListRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieListDao = MovieListDatabase.getInstance(application).movieListDao()

    fun insertOrUpdateMovieList(movieList: CustomMovieList) {
        launch { insertOrUpdateMovieListBG(movieList) }
    }

    private suspend fun insertOrUpdateMovieListBG(movieList: CustomMovieList) {
        withContext(Dispatchers.IO) {
            movieList.updateDate = getCurrentDate()
            movieListDao.insertOrUpdateCustomList(movieList)
        }
    }

    fun addMovieToMovieList(movieList: CustomMovieList, movieRoomId: Int) {
        launch { addMovieToMovieListBG(movieList, movieRoomId) }
    }

    private suspend fun addMovieToMovieListBG(movieList: CustomMovieList, movieRoomId: Int) {
        withContext(Dispatchers.IO) {
            movieList.movieIdList?.let {
                val list: MutableList<Int> = it as MutableList<Int>
                list.add(movieRoomId)
                movieList.movieIdList = list
            } ?: run {
                movieList.movieIdList = listOf(movieRoomId)
            }
            insertOrUpdateMovieListBG(movieList)
        }
    }

    fun getMovieListById(id: Int) = movieListDao.getCustomListById(id)

    suspend fun getAllCustomMovieLists() = movieListDao.getAllCustomMovieLists()

    fun getAllCustomMovieListFlow() = movieListDao.getAllCustomMovieListFlow()


    fun deleteMovieFromList(movieList: CustomMovieList, movieRoomId: Int) {
        launch {
            (movieList.movieIdList as MutableList).remove(movieRoomId)
            insertOrUpdateMovieListBG(movieList)
        }
    }

    fun deleteList(list: CustomMovieList) {
        launch { deleteListBG(list) }
    }

    private suspend fun deleteListBG(list: CustomMovieList) {
        withContext(Dispatchers.IO) {
            movieListDao.deleteCustomList(list)
        }
    }
}