package com.example.moviesearcher.model.repositories

import android.app.Application
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.room.database.CreditsDatabase
import com.example.moviesearcher.model.room.database.ImagesDatabase
import com.example.moviesearcher.model.room.database.MovieDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PersonalMovieRepository(private var application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieDao = MovieDatabase.getInstance(application).movieDao()
    private val imagesDao = ImagesDatabase.getInstance(application).imagesDao()
    private val creditsDao = CreditsDatabase.getInstance(application).creditsDao()

    suspend fun insertOrUpdateMovie(movie: Movie): Long {
        val movieRoomId = movieDao.insertOrUpdateMovie(movie)
        insertOrUpdateImages(movie, movieRoomId.toInt())
        insertOrUpdateCredits(movie, movieRoomId.toInt())
        return movieRoomId
    }

    private suspend fun insertOrUpdateImages(movie: Movie, movieRoomId: Int) {
        val images = MovieRepository().getImages(movie.remoteId).body()
        if (images != null) {
            images.generateBitmaps()
            images.movieRoomId = movieRoomId
            imagesDao.insertOrUpdateImages(images)
        }
    }

    fun getImagesById(movieId: Int) = imagesDao.getImagesById(movieId)

    private suspend fun insertOrUpdateCredits(movie: Movie, movieRoomId: Int) {
        val credits = MovieRepository().getCredits(movie.remoteId).body()
        if (credits != null) {
            credits.generateBitmaps()
            credits.movieRoomId = movieRoomId
            creditsDao.insertOrUpdateCredits(credits)
        }
    }

    fun getCreditsById(movieId: Int) = creditsDao.getCreditsById(movieId)

    fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

    fun getMoviesFromIdList(movieIdList: List<Int>) = movieDao.getMoviesFromIdList(movieIdList)

    fun deleteMovie(movie: Movie) {
        launch { deleteMovieBG(movie) }
    }

    private suspend fun deleteMovieBG(movie: Movie) {
        withContext(Dispatchers.IO) {
            movieDao.deleteMovie(movie)
            imagesDao.deleteImagesByMovieId(movie.roomId)
            creditsDao.deleteCreditsByMovieId(movie.roomId)
        }
    }
}