package com.example.mmdb.ui.discover

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.Navigation
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {

    private val _categories =
        CategoryRepository(application).getCategories().asLiveData(viewModelScope.coroutineContext)

    val categories
        get() = _categories

    private var languageSubcategory: Subcategory? = null
    private var genreSubcategory: Subcategory? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isNetworkAvailable(getApplication()))
                networkUnavailableNotification(getApplication())
        }
    }

    fun onSubcategorySelected(checked: Boolean, category: Category, itemIndex: Int) {
        if (checked && (category.items[itemIndex] as Subcategory).name.isNotBlank()) {
            when (category.name) {
                LANGUAGE_CATEGORY -> languageSubcategory =
                    category.items[itemIndex] as Subcategory
                GENRE_CATEGORY -> genreSubcategory =
                    category.items[itemIndex] as Subcategory
            }
        } else {
            when (category.name) {
                LANGUAGE_CATEGORY -> languageSubcategory = null
                GENRE_CATEGORY -> genreSubcategory = null
            }
        }
    }

    fun onRangeSliderValueChanged(){
        
    }

    fun onConfirmSelectionClicked(view: View, startYear: String, endYear: String) {
        val action = DiscoverFragmentDirections.actionRemoteMovieGridFragment()
        action.movieGridType = DISCOVER_MOVIE_LIST
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