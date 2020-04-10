package com.example.moviesearcher.model.room.repositories

import android.app.Application
import com.example.moviesearcher.model.data.WatchlistMovie
import com.example.moviesearcher.model.room.databases.WatchlistDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WatchlistRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val watchlistMovieDao = WatchlistDatabase.getInstance(application).watchlistMovieDao()

    fun insertOrUpdateMovie(watchlistMovie: WatchlistMovie) {
        launch { insertOrUpdateMovieBG(watchlistMovie) }
    }

    private suspend fun insertOrUpdateMovieBG(watchlistMovie: WatchlistMovie) {
        withContext(Dispatchers.IO) {
            watchlistMovieDao.insertOrUpdateMovie(watchlistMovie)
        }
    }

    fun getAllMovies() = watchlistMovieDao.getAllMovies()

    fun deleteWatchlistMovie(movieId: Int) {
        launch { deleteWatchlistMovieBG(movieId) }
    }

    private suspend fun deleteWatchlistMovieBG(movieId: Int) {
        withContext(Dispatchers.IO) {
            watchlistMovieDao.deleteMovieById(movieId)
        }
    }
}