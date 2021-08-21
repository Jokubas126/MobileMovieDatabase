package com.jokubas.mmdb.model.room.repositories

import android.app.Application
import android.net.Uri
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.room.dao.MovieDao
import com.jokubas.mmdb.model.room.databases.MovieDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class RoomMovieRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieListRepository = CustomMovieListRepository(application)

    private val movieDao: MovieDao = MovieDatabase.getInstance(application).movieDao()

    // -------- INSERTION --------//

    suspend fun insertOrUpdateMovie(
        movie: Movie,
        customMovieList: CustomMovieList
    ) {
        movieDao.insertOrUpdateMovie(movie)
        movieListRepository.addMovieToMovieList(customMovieList, movie.id)
    }

    // -------- GETTERS --------//

    suspend fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

    suspend fun getMoviesFromIdList(movieIdList: List<Int>) =
        movieDao.getMoviesFromIdList(movieIdList)

    // -------- DELETION ---------- //

    fun deleteMovieById(movieId: Int) {
        launch { deleteMovieByIdBG(movieId) }
    }

    private suspend fun deleteMovieByIdBG(movieId: Int) {
        withContext(Dispatchers.IO) {
            movieDao.getMovieById(movieId)?.let { deleteMovieFiles(it) }
            movieDao.deleteMovieById(movieId)
        }
    }

    private fun deleteMovieFiles(movie: Movie) {
        movie.posterImageUriString?.let {
            com.jokubas.mmdb.util.deleteFile(
                File(Uri.parse(it).path!!)
            )
        }
        movie.backdropImageUriString?.let {
            com.jokubas.mmdb.util.deleteFile(
                File(Uri.parse(it).path!!)
            )
        }
    }

}