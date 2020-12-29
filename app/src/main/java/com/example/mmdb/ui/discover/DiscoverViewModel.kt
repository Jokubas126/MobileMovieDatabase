package com.example.mmdb.ui.discover

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.recyclerview.widget.ConcatAdapter
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import com.jokubas.mmdb.util.*
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

    val categoriesAdapter = ObservableField<ConcatAdapter>()

    val categories =
        CategoryRepository(application).getCategories()
            .asLiveData(viewModelScope.coroutineContext).apply {
                observeForever {
                    val adapters = arrayListOf<ItemsExpandableAdapter>()
                    value?.forEach {
                        it?.let { category ->
                            adapters.add(ItemsExpandableAdapter(category))
                        }
                    }
                    categoriesAdapter.set(ConcatAdapter(adapters))
                }
            }

    private var languageSubcategory: Subcategory? = null
    private var genreSubcategory: Subcategory? = null

    init {
        if (!isNetworkAvailable(getApplication()))
            networkUnavailableNotification(getApplication())
    }

    fun onSubcategorySelected(checked: Boolean, categoryName: String, subcategory: Subcategory) {
        if (checked && subcategory.name.isNotBlank()) {
            when (categoryName) {
                LANGUAGE_CATEGORY -> languageSubcategory = subcategory
                GENRE_CATEGORY -> genreSubcategory = subcategory
            }
        } else {
            when (categoryName) {
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

    override fun onCleared() {
        super.onCleared()
        categories.removeObserver {}
    }
}