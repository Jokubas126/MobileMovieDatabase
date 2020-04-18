package com.example.mmdb.ui.movielists.personal.customlists.addtolists

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.mmdb.NavGraphDirections
import com.example.mmdb.R
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.data.Movie
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.CustomMovieListRepository
import com.example.mmdb.model.room.repositories.RoomMovieRepository
import com.example.mmdb.util.isNetworkAvailable
import com.example.mmdb.util.networkUnavailableNotification
import com.example.mmdb.util.showProgressSnackBar
import com.example.mmdb.util.showToast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToListsTaskManager(
    private val application: Application,
    private val root: View,
    private val popupWindow: AddToListsPopupWindow
) : AddToListsPopupWindow.ListsConfirmedClickListener {

    private val remoteMovieRepository = RemoteMovieRepository()
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
                    showProgressSnackBar(
                        root,
                        application.getString(R.string.being_uploaded_to_list)
                    )
                    // get all movie details and insert it only if it's not null
                    val fullMovie = remoteMovieRepository.getMovieDetails(movie.remoteId)
                    fullMovie.finalizeInitialization(application)
                    roomMovieRepository.insertOrUpdateMovie(
                        application,
                        fullMovie,
                        checkedLists
                    )
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

    private fun onMovieInserted() {
        Snackbar.make(
            root,
            root.context.getString(R.string.successfully_uploaded_to_list),
            Snackbar.LENGTH_LONG
        )
            .setAction(root.context.getString(R.string.action_check_lists)) {
                val action = NavGraphDirections.actionGlobalCustomListsFragment()
                Navigation.findNavController(root).navigate(action)
            }.show()
    }
}