package com.jokubas.mmdb.moviedetails.model.repositories

import android.content.Context
import android.net.Uri
import com.jokubas.mmdb.moviedetails.model.entities.Credits
import com.jokubas.mmdb.moviedetails.model.entities.Images
import com.jokubas.mmdb.moviedetails.model.local.databases.CreditsDatabase
import com.jokubas.mmdb.moviedetails.model.local.databases.ImagesDatabase
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.flow.mapNotNull
import java.io.File

class RoomMovieDetailsRepository(
    context: Context
) {

    private val imagesDao = ImagesDatabase.getInstance(context).imagesDao()
    private val creditsDao = CreditsDatabase.getInstance(context).creditsDao()

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