package com.jokubas.mmdb.model.room.repositories

import android.app.Application
import android.net.Uri
import com.jokubas.mmdb.model.data.entities.Credits
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.data.entities.Images
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.room.databases.CreditsDatabase
import com.jokubas.mmdb.model.room.databases.ImagesDatabase
import com.jokubas.mmdb.model.room.databases.MovieDatabase
import com.jokubas.mmdb.util.DataResponse
import com.jokubas.mmdb.util.toDataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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
        movieDao.insertOrUpdateMovie(movie)
        imagesDao.insertOrUpdateImages(images)
        creditsDao.insertOrUpdateCredits(credits)
        movieListRepository.addMovieToMovieList(customMovieList, movie.id)
    }

    // -------- GETTERS --------//

    fun imagesFlow(movieId: Int) = imagesDao.images(movieId).mapNotNull { images ->
        DataResponse.Success(
            value = images
        ).takeUnless { it.value.posterList.isNullOrEmpty() && it.value.backdropList.isNullOrEmpty() }
            ?: DataResponse.Error()
    }

    fun creditsFlow(movieId: Int) =
        creditsDao.credits(movieId).mapNotNull { credits ->
            DataResponse.Success(
                value = credits
            ).takeUnless { it.value.castList.isNullOrEmpty() && it.value.crewList.isNullOrEmpty() }
                ?: DataResponse.Error()
        }

    suspend fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)

    suspend fun getMoviesFromIdList(movieIdList: List<Int>) =
        movieDao.getMoviesFromIdList(movieIdList)

    // -------- DELETION ---------- //

    fun deleteMovieById(movieId: Int) {
        launch { deleteMovieByIdBG(movieId) }
    }

    private suspend fun deleteMovieByIdBG(movieId: Int) {
        withContext(Dispatchers.IO) {
            imagesDao.imagesNow(movieId)?.let { deleteImageFiles(it) }
            imagesDao.deleteImagesByMovieId(movieId)
            creditsDao.creditsNow(movieId)?.let { deleteCreditsFiles(it) }
            creditsDao.deleteCreditsByMovieId(movieId)
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

    private fun deleteImageFiles(images: Images) {
        images.posterList.let { posterList ->
            for (poster in posterList)
                poster.imageUriString?.let {
                    com.jokubas.mmdb.util.deleteFile(
                        File(Uri.parse(it).path!!)
                    )
                }
        }
        images.backdropList.let { backdropList ->
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