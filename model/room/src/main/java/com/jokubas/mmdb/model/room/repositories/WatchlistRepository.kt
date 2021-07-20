package com.jokubas.mmdb.model.room.repositories

import android.app.Application
import com.jokubas.mmdb.model.data.entities.WatchlistMovie
import com.jokubas.mmdb.model.room.databases.WatchlistDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WatchlistRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val watchlistMovieDao = WatchlistDatabase.getInstance(application).watchlistMovieDao()

    fun insertOrUpdateMovie(id: Int) {
        launch { insertOrUpdateMovieBG(id) }
    }

    private suspend fun insertOrUpdateMovieBG(id: Int) {
        withContext(Dispatchers.IO) {
            watchlistMovieDao.insertOrUpdateMovie(
                watchlistMovie = WatchlistMovie(id))
        }
    }

    suspend fun getWatchlistNow() = watchlistMovieDao.getWatchlistNow()

    fun getWatchlistFlow() = watchlistMovieDao.getWatchlistFlow()

    fun getWatchlistIdsFlow() = watchlistMovieDao.getWatchlistIdsFlow()

    fun deleteWatchlistMovie(id: Int) {
        launch { deleteWatchlistMovieBG(id) }
    }

    private suspend fun deleteWatchlistMovieBG(id: Int) {
        withContext(Dispatchers.IO) {
            watchlistMovieDao.deleteMovieById(id)
        }
    }
}