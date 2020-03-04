package com.example.moviesearcher.ui.grid

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.moviesearcher.model.data.Movie
import com.example.moviesearcher.model.data.Subcategory
import com.example.moviesearcher.model.services.MovieDbApiService
import com.example.moviesearcher.model.services.responses.MovieListAsyncResponse
import com.example.moviesearcher.util.BundleUtil
import java.util.*

class MainGridViewModel : ViewModel() {

    private var activity: Activity? = null

    private val _movies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<Boolean>()
    private val _loading = MutableLiveData<Boolean>()

    val movies: LiveData<List<Movie>> = _movies
    val error: LiveData<Boolean> = _error
    val loading: LiveData<Boolean> = _loading

    private var page = 0
    private var isListFull = false
    private var listKey: String? = null
    private var subcategory: Subcategory? = null
    private var startYear: String? = null
    private var endYear: String? = null
    private var searchKey: String? = null

    fun initFetch(activity: Activity?, args: Bundle?) {
        this.activity = activity
        _error.value = false
        _loading.value = true
        isListFull = false
        if (args != null) {
            listKey = args.getString(BundleUtil.KEY_MOVIE_LIST_TYPE)
            subcategory = args.getParcelable(BundleUtil.KEY_SUBCATEGORY)
            startYear = args.getString(BundleUtil.KEY_START_YEAR)
            endYear = args.getString(BundleUtil.KEY_END_YEAR)
            searchKey = args.getString(BundleUtil.KEY_SEARCH_QUERY)
        }
        if (listKey == null && subcategory == null && searchKey == null) listKey = BundleUtil.KEY_POPULAR
        if (page == 0) {
            page = 1
            clearAll()
            getMovieList()
        } else {
            _loading.setValue(false)
        }
    }

    fun refresh() {
        isListFull = false
        _error.value = false
        _loading.value = true
        page = 1
        clearAll()
        getMovieList()
    }

    fun fetch() {
        if (!isListFull) {
            _error.value = false
            _loading.value = true
            page++
            getMovieList()
        }
    }

    private fun getMovieList() {
        Thread(Runnable {
            MovieDbApiService().getMovieList(listKey, subcategory, searchKey, startYear, endYear, page,
                    MovieListAsyncResponse {
                        if (it == null) {
                            activity!!.runOnUiThread {
                                isListFull = true
                                if (_movies.value!!.isEmpty())
                                    _error.value = true
                                _loading.value = false
                            }
                        } else {
                            activity!!.runOnUiThread {
                                _movies.value = it
                                _error.value = false
                                _loading.value = false
                            }
                        }
                    })
        }).start()
    }

    private fun clearAll() {
        _movies.value = ArrayList()
    }

    fun onMovieClicked(v: View?, movieId: Int) {
        val action: NavDirections = MainGridFragmentDirections.actionMovieDetails(movieId)
        Navigation.findNavController(v!!).navigate(action)
    }
}