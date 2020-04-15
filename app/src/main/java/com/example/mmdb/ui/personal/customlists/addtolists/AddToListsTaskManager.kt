package com.example.mmdb.ui.personal.customlists.addtolists

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.mmdb.NavGraphDirections
import com.example.mmdb.R
import com.example.mmdb.model.data.CustomMovieList
import com.example.mmdb.model.data.Movie
import com.example.mmdb.model.remote.repositories.RemoteMovieRepository
import com.example.mmdb.model.room.repositories.MovieListRepository
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
    private val popupWindow: AddToListsPopupWindow
) : AddToListsPopupWindow.ListsConfirmedClickListener {

    private val remoteMovieRepository = RemoteMovieRepository()
    private val movieListRepository = MovieListRepository(application)
    private val roomMovieRepository = RoomMovieRepository(application)

    init {
        popupWindow.setListsConfirmedClickListener(this)
        getCustomLists()
    }

    private fun getCustomLists() {
        CoroutineScope(Dispatchers.IO).launch {
            val customLists =  movieListRepository.getAllCustomMovieLists()
            withContext(Dispatchers.Main){
                popupWindow.setupLists(customLists)
            }
        }
    }

    override fun onConfirmListsClicked(
        movie: Movie,
        checkedLists: List<CustomMovieList>,
        root: View
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
                    // get all movie details and add it only if it's not null
                    remoteMovieRepository.getMovieDetails(movie.remoteId).body()?.let {
                        it.finalizeInitialization(application)
                        val movieRoomId = roomMovieRepository.insertOrUpdateMovie(application, it)
                        for (list in checkedLists)
                            movieListRepository.addMovieToMovieList(
                                list,
                                movieRoomId.toInt()
                            )
                        onMovieAdded(root)
                    } ?: run {
                        false
                    }
                }
                true
            }
            else -> {
                networkUnavailableNotification(application)
                false
            }
        }
    }

    private fun onMovieAdded(root: View){
        CoroutineScope(Dispatchers.Main).launch {
            Snackbar.make(
                root,
                application.getString(R.string.successfully_uploaded_to_list),
                Snackbar.LENGTH_LONG
            )
                .setAction(application.getString(R.string.action_check_lists)) {
                    val action = NavGraphDirections.actionGlobalCustomListsFragment()
                    Navigation.findNavController(root).navigate(action)
                }.show()
        }
    }
}