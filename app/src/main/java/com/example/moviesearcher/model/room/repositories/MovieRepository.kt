package com.example.moviesearcher.model.room.repositories

import android.app.Application
import android.content.Context
import android.net.Uri
import com.example.moviesearcher.model.data.Credits
import com.example.moviesearcher.model.data.Images
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.remote.repositories.MovieRepository
import com.example.moviesearcher.model.room.database.CreditsDatabase
import com.example.moviesearcher.model.room.database.ImagesDatabase
import com.example.moviesearcher.model.room.database.MovieDatabase
import com.example.moviesearcher.util.deleteFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class MovieRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieDao = MovieDatabase.getInstance(application).movieDao()
    private val imagesDao = ImagesDatabase.getInstance(application).imagesDao()
    private val creditsDao = CreditsDatabase.getInstance(application).creditsDao()

    suspend fun insertOrUpdateMovie(context: Context, movie: Movie): Long {
        val movieRoomId = movieDao.insertOrUpdateMovie(movie)
        insertOrUpdateImages(context, movie, movieRoomId.toInt())
        insertOrUpdateCredits(context, movie, movieRoomId.toInt())
        return movieRoomId
    }

    private suspend fun insertOrUpdateImages(context: Context, movie: Movie, movieRoomId: Int) {
        val images = MovieRepository()
            .getImages(movie.remoteId).body()
        if (images != null) {
            images.generateFileUris(context)
            images.movieRoomId = movieRoomId
            imagesDao.insertOrUpdateImages(images)
        }
    }

    fun getImagesById(movieId: Int) = imagesDao.getImageLiveDataById(movieId)

    private suspend fun insertOrUpdateCredits(context: Context, movie: Movie, movieRoomId: Int) {
        val credits = MovieRepository()
            .getCredits(movie.remoteId).body()
        if (credits != null) {
            credits.generateFileUris(context)
            credits.movieRoomId = movieRoomId
            creditsDao.insertOrUpdateCredits(credits)
        }
    }

    fun getCreditsById(movieId: Int) = creditsDao.getCreditsLiveDataById(movieId)

    fun getMovieById(movieId: Int) = movieDao.getMovieLiveDataById(movieId)

    fun getMoviesFromIdList(movieIdList: List<Int>) = movieDao.getMoviesFromIdList(movieIdList)

    fun deleteMovieById(movieId: Int) {
        launch { deleteMovieByIdBG(movieId) }
    }

    private suspend fun deleteMovieByIdBG(movieId: Int) {
        withContext(Dispatchers.IO) {
            deleteImageFiles(imagesDao.getImagesById(movieId))
            imagesDao.deleteImagesByMovieId(movieId)
            deleteCreditsFiles(creditsDao.getCreditsById(movieId))
            creditsDao.deleteCreditsByMovieId(movieId)
            deleteMovieFiles(movieDao.getMovieById(movieId))
            movieDao.deleteMovieById(movieId)
        }
    }

    fun deleteMovie(movie: Movie) {
        launch { deleteMovieBG(movie) }
    }

    private suspend fun deleteMovieBG(movie: Movie) {
        withContext(Dispatchers.IO) {
            deleteImageFiles(imagesDao.getImagesById(movie.roomId))
            imagesDao.deleteImagesByMovieId(movie.roomId)
            deleteCreditsFiles(creditsDao.getCreditsById(movie.roomId))
            creditsDao.deleteCreditsByMovieId(movie.roomId)
            deleteMovieFiles(movie)
            movieDao.deleteMovie(movie)
        }
    }

    private fun deleteMovieFiles(movie: Movie) {
        if (movie.posterImageUriString != null)
            deleteFile(File(Uri.parse(movie.posterImageUriString).path!!))
        if (movie.backdropImageUriString != null)
            deleteFile(File(Uri.parse(movie.backdropImageUriString).path!!))
    }

    private fun deleteImageFiles(images: Images) {
        if (!images.posterList.isNullOrEmpty())
            for (poster in images.posterList)
                if (poster.imageUriString != null)
                    deleteFile(File(Uri.parse(poster.imageUriString).path!!))
        if (!images.backdropList.isNullOrEmpty())
            for (backdrop in images.backdropList)
                if (backdrop.imageUriString != null)
                    deleteFile(File(Uri.parse(backdrop.imageUriString).path!!))
    }

    private fun deleteCreditsFiles(credits: Credits) {
        if (!credits.castList.isNullOrEmpty())
            for (person in credits.castList)
                if (person.profileImageUriString != null)
                    deleteFile(File(Uri.parse(person.profileImageUriString).path!!))
        if (!credits.crewList.isNullOrEmpty())
            for (person in credits.crewList)
                if (person.profileImageUriString != null)
                    deleteFile(File(Uri.parse(person.profileImageUriString).path!!))
    }
}