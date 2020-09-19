package com.jokubas.mmdb.model.room.repositories

import android.app.Application
import android.net.Uri
import com.jokubas.mmdb.model.data.dataclasses.Credits
import com.jokubas.mmdb.model.data.dataclasses.CustomMovieList
import com.jokubas.mmdb.model.data.dataclasses.Images
import com.jokubas.mmdb.model.data.dataclasses.Movie
import com.jokubas.mmdb.model.room.databases.CreditsDatabase
import com.jokubas.mmdb.model.room.databases.ImagesDatabase
import com.jokubas.mmdb.model.room.databases.MovieDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class RoomMovieRepository(application: Application) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val movieListRepository = CustomMovieListRepository(application)

    private val movieDao = MovieDatabase.getInstance(application).movieDao()
    private val imagesDao = ImagesDatabase.getInstance(application).imagesDao()
    private val creditsDao = CreditsDatabase.getInstance(application).creditsDao()

    // -------- INSERTION --------//

    suspend fun insertOrUpdateMovie(
        movie: Movie,
        images: Images,
        credits: Credits,
        customMovieList: CustomMovieList
    ) {
        val movieRoomId = movieDao.insertOrUpdateMovie(movie).toInt()
        images.movieRoomId = movieRoomId
        credits.movieRoomId = movieRoomId
        imagesDao.insertOrUpdateImages(images)
        creditsDao.insertOrUpdateCredits(credits)
        movieListRepository.addMovieToMovieList(customMovieList, movieRoomId)
    }

    // -------- GETTERS --------//

    suspend fun getImagesById(movieId: Int) = imagesDao.getImagesById(movieId)

    fun getCreditsFlowById(movieId: Int) = flow {
        emit(creditsDao.getCreditsById(movieId))
    }

    suspend fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

    suspend fun getMoviesFromIdList(movieIdList: List<Int>) =
        movieDao.getMoviesFromIdList(movieIdList)

    // -------- DELETION ---------- //

    fun deleteMovieById(movieId: Int) {
        launch { deleteMovieByIdBG(movieId) }
    }

    private suspend fun deleteMovieByIdBG(movieId: Int) {
        withContext(Dispatchers.Default) {
            deleteImageFiles(imagesDao.getImagesById(movieId))
            imagesDao.deleteImagesByMovieId(movieId)
            deleteCreditsFiles(creditsDao.getCreditsById(movieId))
            creditsDao.deleteCreditsByMovieId(movieId)
            deleteMovieFiles(movieDao.getMovieById(movieId))
            movieDao.deleteMovieById(movieId)
        }
    }

    private fun deleteMovieFiles(movie: Movie) {
        movie.posterImageUriString?.let {
            com.jokubas.mmdb.util.deleteFile(
                File(
                    Uri.parse(it).path!!
                )
            )
        }
        movie.backdropImageUriString?.let {
            com.jokubas.mmdb.util.deleteFile(
                File(
                    Uri.parse(it).path!!
                )
            )
        }
    }

    private fun deleteImageFiles(images: Images) {
        images.posterList?.let { posterList ->
            for (poster in posterList)
                poster.imageUriString?.let {
                    com.jokubas.mmdb.util.deleteFile(
                        File(Uri.parse(it).path!!)
                    )
                }
        }
        images.backdropList?.let { backdropList ->
            for (backdrop in backdropList)
                backdrop.imageUriString?.let {
                    com.jokubas.mmdb.util.deleteFile(
                        File(Uri.parse(it).path!!)
                    )
                }
        }
    }

    private fun deleteCreditsFiles(credits: Credits) {
        credits.castList?.let { castList ->
            for (person in castList)
                person.profileImageUriString?.let {
                    com.jokubas.mmdb.util.deleteFile(
                        File(Uri.parse(it).path!!)
                    )
                }
        }
        credits.crewList?.let { crewList ->
            for (person in crewList)
                person.profileImageUriString?.let {
                    com.jokubas.mmdb.util.deleteFile(
                        File(Uri.parse(it).path!!)
                    )
                }
        }
    }
}