package com.example.mmdb.model.room.repositories

import android.app.Application
import android.content.Context
import android.net.Uri
import com.example.mmdb.model.data.Credits
import com.example.mmdb.model.data.Images
import com.example.mmdb.model.data.Movie
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.databases.CreditsDatabase
import com.example.mmdb.model.room.databases.ImagesDatabase
import com.example.mmdb.model.room.databases.MovieDatabase
import com.example.mmdb.util.deleteFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class RoomMovieRepository(application: Application) : CoroutineScope {
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
        // insert if the images are retrieved not null from remote
        RemoteMovieRepository().getImages(movie.remoteId).body()?.let {
                it.generateFileUris(context)
                it.movieRoomId = movieRoomId
                imagesDao.insertOrUpdateImages(it)
            }
    }

    fun getImagesById(movieId: Int) = imagesDao.getImageLiveDataById(movieId)

    private suspend fun insertOrUpdateCredits(context: Context, movie: Movie, movieRoomId: Int) {
        // insert if the credits are retrieved not null from remote
        RemoteMovieRepository().getCredits(movie.remoteId).body()?.let {
            it.generateFileUris(context)
            it.movieRoomId = movieRoomId
            creditsDao.insertOrUpdateCredits(it)
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

    private fun deleteMovieFiles(movie: Movie) {
        movie.posterImageUriString?.let { deleteFile(File(Uri.parse(it).path!!)) }
        movie.backdropImageUriString?.let { deleteFile(File(Uri.parse(it).path!!)) }
    }

    private fun deleteImageFiles(images: Images) {
        if (!images.posterList.isNullOrEmpty())
            for (poster in images.posterList)
                poster.imageUriString?.let { deleteFile(File(Uri.parse(it).path!!)) }
        if (!images.backdropList.isNullOrEmpty())
            for (backdrop in images.backdropList)
                backdrop.imageUriString?.let { deleteFile(File(Uri.parse(it).path!!)) }
    }

    private fun deleteCreditsFiles(credits: Credits) {
        if (!credits.castList.isNullOrEmpty())
            for (person in credits.castList)
                person.profileImageUriString?.let { deleteFile(File(Uri.parse(it).path!!)) }
        if (!credits.crewList.isNullOrEmpty())
            for (person in credits.crewList)
                person.profileImageUriString?.let { deleteFile(File(Uri.parse(it).path!!)) }
    }
}