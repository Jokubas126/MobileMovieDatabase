package com.example.mmdb.ui.discover

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.example.mmdb.model.data.Category
import com.example.mmdb.model.data.Genres
import com.example.mmdb.model.data.Subcategory
import com.example.mmdb.model.remote.repositories.CategoryRepository
import com.example.mmdb.util.*
import com.example.mmdb.util.managers.ProgressManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.sort

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {

    private val progressManager = ProgressManager()

    private val _categories = MutableLiveData<MutableList<Category>>()

    val categories: LiveData<MutableList<Category>> = _categories
    val loading: LiveData<Boolean> = progressManager.loading
    val error: LiveData<Boolean> = progressManager.error

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (isNetworkAvailable(getApplication())) {
                progressManager.loading()
                getLanguages()
                getGenres()
            } else {
                progressManager.error()
                networkUnavailableNotification(getApplication())
            }
        }
    }

    private fun getLanguages() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = CategoryRepository().getLanguages()
            if (response.isSuccessful)
                response.body()?.let {
                    insertCategoriesToLiveData(LANGUAGE_CATEGORY, formatSubcategories(it))
                } ?: run { progressManager.error() }
            else
                progressManager.error()
        }
    }

    private fun getGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = CategoryRepository().getGenres()
            if (response.isSuccessful)
                response.body()?.let {
                    insertCategoriesToLiveData(
                        GENRE_CATEGORY,
                        formatSubcategories(genresToSubcategoryList(it))
                    )
                } ?: run { progressManager.error() }
            else
                progressManager.error()
        }
    }

    private fun genresToSubcategoryList(genres: Genres): List<Subcategory> {
        val subcategoryList = mutableListOf<Subcategory>()
        for (genre in genres.genreList) {
            subcategoryList.add(Subcategory(genre.id.toString(), genre.name))
        }
        return subcategoryList
    }

    private fun formatSubcategories(list: List<Subcategory>): List<Subcategory> {
        sort(list) { o1: Subcategory, o2: Subcategory -> o1.name.compareTo(o2.name) }
        // to have an empty item at the beginning for deselection
        (list as MutableList<Subcategory>).add(0, Subcategory("", ""))
        return list
    }

    private fun insertCategoriesToLiveData(
        categoryName: String,
        subcategoryList: List<Subcategory>
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val list = categories.value?: run{ mutableListOf<Category>() }
            list.add(Category(categoryName, subcategoryList))
            _categories.value = list
            progressManager.retrieved()
        }
    }

    // ------------ Navigation related -------------//

    private var languageSubcategory: Subcategory? = null
    private var genreSubcategory: Subcategory? = null

    fun onSubcategorySelected(checked: Boolean, category: Category, itemIndex: Int) {
        if (checked) {
            if ((category.items[itemIndex] as Subcategory).name.isBlank()) {
                when (category.name) {
                    LANGUAGE_CATEGORY -> languageSubcategory = null
                    GENRE_CATEGORY -> genreSubcategory = null
                }
            } else {
                when (category.name) {
                    LANGUAGE_CATEGORY -> languageSubcategory =
                        category.items[itemIndex] as Subcategory
                    GENRE_CATEGORY -> genreSubcategory =
                        category.items[itemIndex] as Subcategory
                }
            }
        } else {
            when (category.name) {
                LANGUAGE_CATEGORY -> languageSubcategory = null
                GENRE_CATEGORY -> genreSubcategory = null
            }
        }
    }

    fun onConfirmSelectionClicked(view: View, startYear: String, endYear: String) {
        val action = DiscoverFragmentDirections.actionRemoteMovieGridFragment()
        action.movieGridType = DISCOVER_MOVIE_GRID
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