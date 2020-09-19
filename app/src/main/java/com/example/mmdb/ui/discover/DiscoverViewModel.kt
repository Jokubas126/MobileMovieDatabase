package com.example.mmdb.ui.discover

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.jokubas.mmdb.model.data.dataclasses.Category
import com.jokubas.mmdb.model.data.dataclasses.Subcategory
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {

    private val _categories =
        CategoryRepository(application)
            .getCategories().asLiveData(viewModelScope.coroutineContext)

    val categories
        get() = _categories

    private var languageSubcategory: Subcategory? = null
    private var genreSubcategory: Subcategory? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isNetworkAvailable(getApplication()))
                networkUnavailableNotification(
                    getApplication()
                )
        }
    }

    fun onSubcategorySelected(checked: Boolean, category: Category, itemIndex: Int) {
        if (checked) {
            if ((category.items[itemIndex] as Subcategory).name.isBlank()) {
                when (category.name) {
                    com.jokubas.mmdb.util.LANGUAGE_CATEGORY -> languageSubcategory = null
                    com.jokubas.mmdb.util.GENRE_CATEGORY -> genreSubcategory = null
                }
            } else {
                when (category.name) {
                    com.jokubas.mmdb.util.LANGUAGE_CATEGORY -> languageSubcategory =
                        category.items[itemIndex] as Subcategory
                    com.jokubas.mmdb.util.GENRE_CATEGORY -> genreSubcategory =
                        category.items[itemIndex] as Subcategory
                }
            }
        } else {
            when (category.name) {
                com.jokubas.mmdb.util.LANGUAGE_CATEGORY -> languageSubcategory = null
                com.jokubas.mmdb.util.GENRE_CATEGORY -> genreSubcategory = null
            }
        }
    }

    fun onConfirmSelectionClicked(view: View, startYear: String, endYear: String) {
        val action = DiscoverFragmentDirections.actionRemoteMovieGridFragment()
        action.movieGridType = com.jokubas.mmdb.util.DISCOVER_MOVIE_LIST
        action.startYear =
            if (startYear == "∞") null
            else startYear
        action.endYear = endYear
        val discoveryArrayList = arrayListOf("$startYear - $endYear")
        genreSubcategory?.let {
            action.genreId = Integer.parseInt(it.code)
            discoveryArrayList.add(it.name)
        }
        languageSubcategory?.let {
            action.languageKey = it.code
            discoveryArrayList.add(it.name)
        }
        if (discoveryArrayList.isNotEmpty())
            action.discoverNameArray = discoveryArrayList.toTypedArray()
        Navigation.findNavController(view).navigate(action)
    }
}