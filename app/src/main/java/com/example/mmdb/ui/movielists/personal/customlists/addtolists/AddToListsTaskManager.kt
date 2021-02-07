package com.example.mmdb.ui.movielists.personal.customlists.addtolists

import android.app.Application
import android.widget.Toast
import com.example.mmdb.R
import com.example.mmdb.config.requireAppConfig
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import com.jokubas.mmdb.util.showToast
import com.jokubas.mmdb.model.data.entities.CustomMovieList
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.model.room.repositories.CustomMovieListRepository
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO set this part up using Toast and popup notification with loading
class AddToListsTaskManager(
    private val application: Application,
    //private val root: View,
    private val popupWindow: AddToListsPopupWindow
) : AddToListsPopupWindow.ListsConfirmedClickListener {

    private val remoteMovieRepository = application.requireAppConfig().movieConfig.remoteMovieRepository
    private val movieListRepository = CustomMovieListRepository(application)
    private val roomMovieRepository = RoomMovieRepository(application)

    init {
        popupWindow.setListsConfirmedClickListener(this)
        getCustomLists()
    }

    private fun getCustomLists() {
        CoroutineScope(Dispatchers.IO).launch {
            val customLists = movieListRepository.getAllCustomMovieLists()
            withContext(Dispatchers.Main) {
                popupWindow.setupLists(customLists)
            }
        }
    }

    override fun onConfirmListsClicked(
        movie: Movie,
        checkedLists: List<CustomMovieList>
    ): Boolean {
        return when {
            checkedLists.isNullOrEmpty() -> {
                showToast(
                    application,
                    application.getString(R.string.select_a_list),
                    Toast.LENGTH_SHORT
                )
                false
            }
            isNetworkAvailable(application) -> {
                CoroutineScope(Dispatchers.IO).launch {
                    /*showProgressSnackBar(
                        root,
                        application.getString(R.string.being_uploaded_to_list)
                    )*/
                    insertMovie(movie, checkedLists)
                    onMovieInserted()
                }
                true
            }
            else -> {
                networkUnavailableNotification(application)
                false
            }
        }
    }

    private suspend fun insertMovie(movie: Movie, checkedLists: List<CustomMovieList>) {
        // to get all the details of movie
        try {
            val fullMovie = remoteMovieRepository.getMovieDetails(movie.remoteId)
            fullMovie.finalizeInitialization(application)
            for (list in checkedLists) {
                roomMovieRepository.insertOrUpdateMovie(
                    fullMovie,
                    getImages(fullMovie),
                    getCredits(fullMovie),
                    list
                )
            }
        } catch (e: Exception) {
            onInsertFailed()
        }
    }

    private suspend fun getImages(movie: Movie) =
        remoteMovieRepository.getImages(movie.remoteId).apply {
            generateFileUris(application)
        }

    private suspend fun getCredits(movie: Movie) =
        remoteMovieRepository.getCredits(movie.remoteId).apply {
            generateFileUris(application)
        }


    private fun onMovieInserted() {
        /*Snackbar.make(
            root,
            root.context.getString(R.string.successfully_uploaded_to_list),
            Snackbar.LENGTH_LONG
        )
            .setAction(root.context.getString(R.string.action_check_lists)) {
                val action = NavGraphDirections.actionGlobalCustomListsFragment()
                Navigation.findNavController(root).navigate(action)
            }.show()*/
    }

    private fun onInsertFailed() {
        /*Snackbar.make(
            root,
            root.context.getString(R.string.failed_to_upload_to_list),
            Snackbar.LENGTH_LONG
        ).show()*/
    }
}