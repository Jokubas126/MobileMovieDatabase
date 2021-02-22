package com.example.mmdb.ui.discover

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.mmdb.config.AppConfig
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.DiscoverFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.navigation.actions.RemoteMovieGridFragmentAction
import com.example.mmdb.ui.ToolbarViewModel
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory
import java.util.*

class DiscoverViewModel(
    private val appConfig: AppConfig,
    private val navigationController: NavigationController,
    private val discoverFragmentAction: DiscoverFragmentAction,
    private val discoverFragmentConfig: DiscoverFragmentConfig,
    val toolbarViewModel: ToolbarViewModel
) : ViewModel() {

    companion object {
        @JvmStatic
        val INITIAL_START_YEAR_VALUE = 1950

        @JvmStatic
        val INITIAL_END_YEAR_VALUE = Calendar.getInstance().get(Calendar.YEAR)
    }

    val startYear = ObservableInt(INITIAL_START_YEAR_VALUE)
    val endYear = ObservableInt(INITIAL_END_YEAR_VALUE)

    val categoriesAdapter = ObservableField<ConcatAdapter>()

    private val categories = appConfig.movieConfig.categoryRepository.getCategories()
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
        setupConfirmButton()
        if (!appConfig.networkCheckConfig.isNetworkAvailable())
            appConfig.networkCheckConfig.networkUnavailableNotification()
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

    private fun setupConfirmButton() {
        toolbarViewModel.setClickListener(object : ToolbarViewModel.ClickListener {
            override fun onConfirmClicked() {
                super.onConfirmClicked()
                appConfig.toolbarConfig.setBackFragment()
                navigationController.goTo(
                    action = RemoteMovieGridFragmentAction(
                        MovieListType.Discover(
                            startYear = if (startYear.get() == INITIAL_START_YEAR_VALUE) null
                            else startYear.get().toString(),
                            endYear = endYear.get().toString(),
                            genreKeys = genreSubcategory.map { it.code },
                            languageKeys = languageSubcategory.map { it.code }
                        )
                    ),
                    animation = NavigationController.Animation.FromRight
                )
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        categories.removeObserver {}
    }
}