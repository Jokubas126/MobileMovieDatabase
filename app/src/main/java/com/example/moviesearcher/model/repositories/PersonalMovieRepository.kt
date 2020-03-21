package com.example.moviesearcher.model.repositories

import android.app.Application
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.room.database.ImagesDatabase
import com.example.moviesearcher.model.room.database.MovieDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PersonalMovieRepository(application: Application): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieDao = MovieDatabase.getInstance(application).movieDao()
    private val imagesDao = ImagesDatabase.getInstance(application).imagesDao()

    suspend fun insertOrUpdateMovie(movie: Movie): Long {
        val movieRoomId = movieDao.insertOrUpdateMovie(movie)
        insertOrUpdateImages(movie, movieRoomId.toInt())
        return movieRoomId
    }

    private suspend fun insertOrUpdateImages(movie: Movie, movieRoomId: Int){
        val images = MovieRepository().getImages(movie.remoteId).body()
        if (images != null){
            images.generateBitmaps()
            images.movieRoomId = movieRoomId
            imagesDao.insertOrUpdateMovie(images)
        }
    }

    fun getImagesById(movieId: Int) = imagesDao.getImagesById(movieId)

    fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

    fun getMoviesFromIdList(movieIdList: List<Int>) = movieDao.getMoviesFromIdList(movieIdList)

    fun deleteMovie(movie: Movie){
        launch { deleteMovieBG(movie) }
    }

    private suspend fun deleteMovieBG(movie: Movie){
        withContext(Dispatchers.IO){
            movieDao.deleteMovie(movie)
        }
    }
}