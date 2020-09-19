package com.jokubas.mmdb.model.room.repositories

import android.app.Application
import com.jokubas.mmdb.model.data.dataclasses.CustomMovieList
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CustomMovieListRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieListDao = com.jokubas.mmdb.model.room.databases.MovieListDatabase.getInstance(application).movieListDao()

    suspend fun insertOrUpdateMovieList(movieList: CustomMovieList) {
        movieList.updateDate = com.jokubas.mmdb.util.getCurrentDate()
        movieListDao.insertOrUpdateCustomList(movieList)
    }

    suspend fun addMovieToMovieList(movieList: CustomMovieList, movieRoomId: Int) {
        movieList.movieIdList?.let {
            val list = mutableListOf<Int>()
            list.addAll(it)
            list.add(movieRoomId)
            movieList.movieIdList = list
        } ?: run {
            movieList.movieIdList = listOf(movieRoomId)
        }
        insertOrUpdateMovieList(movieList)
    }

    fun getMovieListById(id: Int) = movieListDao.getCustomListById(id)

    suspend fun getAllCustomMovieLists() = movieListDao.getAllCustomMovieLists()

    fun getAllCustomMovieListFlow() = movieListDao.getAllCustomMovieListFlow()


    fun deleteMovieFromList(movieList: CustomMovieList, movieRoomId: Int) {
        launch {
            (movieList.movieIdList as MutableList).remove(movieRoomId)
            insertOrUpdateMovieList(movieList)
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