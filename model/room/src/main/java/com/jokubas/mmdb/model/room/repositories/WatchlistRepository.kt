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

    fun getWatchlist() = watchlistMovieDao.getWatchlist()

    fun deleteWatchlistMovie(movieId: Int) {
        launch { deleteWatchlistMovieBG(movieId) }
    }

    private suspend fun deleteWatchlistMovieBG(movieId: Int) {
        withContext(Dispatchers.IO) {
            watchlistMovieDao.deleteMovieById(movieId)
        }
    }
}