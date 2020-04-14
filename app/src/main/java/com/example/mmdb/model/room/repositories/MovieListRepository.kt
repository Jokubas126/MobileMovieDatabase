package com.example.mmdb.model.room.repositories

import android.app.Application
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.room.databases.MovieListDatabase
import com.example.mmdb.util.getCurrentDate
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListRepository(application: Application) : CoroutineScope {
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

    fun addMovieToMovieList(movieList: CustomMovieList, movieRoomId: Int){
        launch { addMovieToMovieListBG(movieList, movieRoomId) }
    }

    private suspend fun addMovieToMovieListBG(movieList: CustomMovieList, movieRoomId: Int){
        withContext(Dispatchers.IO){
            if (!movieList.movieIdList.isNullOrEmpty()){
                val list: MutableList<Int> = movieList.movieIdList!! as MutableList
                list.add(movieRoomId)
                movieList.movieIdList = list
            } else
                movieList.movieIdList = listOf(movieRoomId)
            insertOrUpdateMovieListBG(movieList)
        }
    }

    fun getMovieListById(id: Int) = movieListDao.getCustomListById(id)

    fun getAllMovieLists() = movieListDao.getAllCustomMovieLists()

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

    fun deleteAllLists() {
        launch { deleteAllListsBG() }
    }

    private suspend fun deleteAllListsBG(){
        withContext(Dispatchers.IO){
            movieListDao.deleteAllCustomLists()
        }
    }
}