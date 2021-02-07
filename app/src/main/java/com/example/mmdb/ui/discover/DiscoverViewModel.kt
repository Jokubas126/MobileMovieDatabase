package com.example.mmdb.ui.discover

import android.app.Application
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.mmdb.config.requireAppConfig
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory
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

    private val categories = application.requireAppConfig().movieConfig.categoryRepository.getCategories()
        .asLiveData(viewModelScope.coroutineContext).apply {
            observeForever { categoryList ->
                val adapters = arrayListOf<ItemsExpandableAdapter>()
                categoryList.forEach { category ->
                    adapters.add(ItemsExpandableAdapter(category)
                    { categoryType, subcategory ->
                        onSubcategoryClicked(categoryType, subcategory)
                    })
                }
                categoriesAdapter.set(ConcatAdapter(adapters))
            }
        }

    private var languageSubcategory = mutableListOf<Subcategory>()
    private var genreSubcategory = mutableListOf<Subcategory>()

    init {
        if (!isNetworkAvailable(getApplication()))
            networkUnavailableNotification(getApplication())
    }

    private fun onSubcategoryClicked(
        categoryType: CategoryType,
        subcategory: Subcategory
    ) {
        if (subcategory.isChecked) {
            when (categoryType) {
                CategoryType.LANGUAGES -> languageSubcategory.addSubcategory(subcategory)
                CategoryType.GENRES -> genreSubcategory.addSubcategory(subcategory)
            }
        } else {
            when (categoryType) {
                CategoryType.LANGUAGES -> languageSubcategory.remove(subcategory)
                CategoryType.GENRES -> genreSubcategory.remove(subcategory)
            }
        }
    }

    private fun MutableList<Subcategory>.addSubcategory(subcategory: Subcategory) {
        if (!contains(subcategory))
            add(subcategory)
    }

    fun onRangeSliderValueChanged(isLeftThumb: Boolean, value: Int) {
        when (isLeftThumb) {
            true -> startYear.set(value)
            else -> endYear.set(value)
        }
    }

    fun onConfirmSelectionClicked(navController: NavController) {
        /*val action = DiscoverFragmentDirections.actionRemoteMovieGridFragment()
        action.movieGridType = DISCOVER_MOVIE_LIST
        action.startYear =
            if (startYear.get() == INITIAL_START_YEAR_VALUE) null
            else startYear.get().toString()
        action.endYear = endYear.get().toString()

        action.genreKeys = genreSubcategory.map { it.code }.toTypedArray()
        action.languageKeys = languageSubcategory.map { it.code }.toTypedArray()

        arrayListOf<String>().apply {
            action.startYear?.let { add("From: $it") }
            add("To: ${action.endYear}")
            addAll(genreSubcategory.map { it.name })
            addAll(languageSubcategory.map { it.name })
            if (isNotEmpty())
                action.discoverNameArray = this.toTypedArray()
        }

        navController.navigate(action)*/
    }

    override fun onCleared() {
        super.onCleared()
        categories.removeObserver {}
    }
}