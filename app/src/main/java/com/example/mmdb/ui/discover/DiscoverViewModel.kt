package com.example.mmdb.ui.discover

import android.app.Application
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jokubas.mmdb.model.data.entities.Category
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.util.*
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Year
import java.time.ZonedDateTime
import java.util.*

class DiscoverViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        @JvmStatic
        val INITIAL_START_YEAR_VALUE = 1950

        @JvmStatic
        val INITIAL_END_YEAR_VALUE = Calendar.getInstance().get(Calendar.YEAR)
    }

    val startYear = ObservableInt(INITIAL_START_YEAR_VALUE)
    val endYear = ObservableInt(INITIAL_END_YEAR_VALUE)

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

    fun onRangeSliderValueChanged(isLeftThumb: Boolean, value: Int) {
        when (isLeftThumb) {
            true -> startYear.set(value)
            else -> endYear.set(value)
        }
    }

    fun onConfirmSelectionClicked(navController: NavController) {
        val action = DiscoverFragmentDirections.actionRemoteMovieGridFragment()
        action.movieGridType = DISCOVER_MOVIE_LIST
        action.startYear =
            if (startYear.get() == INITIAL_START_YEAR_VALUE) null
            else startYear.get().toString()
        action.endYear = endYear.get().toString()
        val discoveryArrayList = arrayListOf("${startYear.get()} - ${endYear.get()}")
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
        navController.navigate(action)
    }
}