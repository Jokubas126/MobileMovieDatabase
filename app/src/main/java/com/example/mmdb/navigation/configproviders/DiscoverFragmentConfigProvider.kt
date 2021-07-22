package com.example.mmdb.navigation.configproviders

import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.ToolbarViewModel
import com.example.mmdb.ui.discover.DiscoverFragmentConfig
import com.jokubas.mmdb.model.data.entities.CategoryType
import com.jokubas.mmdb.model.data.entities.Subcategory
import com.jokubas.mmdb.model.remote.repositories.CategoryRepository
import kotlinx.coroutines.flow.*
import java.util.*

class DiscoverFragmentConfigProvider : ConfigProvider<DiscoverFragmentConfig> {

    companion object {
        @JvmStatic
        val INITIAL_START_YEAR_VALUE = 1950

        @JvmStatic
        val INITIAL_END_YEAR_VALUE = Calendar.getInstance().get(Calendar.YEAR)
    }

    private val startYear = MutableStateFlow(INITIAL_START_YEAR_VALUE)
    private val endYear = MutableStateFlow(INITIAL_END_YEAR_VALUE)

    private var languageSubcategory = mutableListOf<Subcategory>()
    private var genreSubcategory = mutableListOf<Subcategory>()

    override fun config(fragment: Fragment): DiscoverFragmentConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val navController: NavigationController = fragment.requireNavController()

        val categoryRepository: CategoryRepository = appConfig.movieConfig.categoryRepository

        return DiscoverFragmentConfig(
            provideCategories = {
                categoryRepository.categories()
            },
            startYearFlow = startYear as StateFlow<Int>,
            endYearFlow = endYear as StateFlow<Int>,
            onRangeSliderValueChangedListener = { slider, value, _ ->
                when (slider.activeThumbIndex == 0) {
                    true -> startYear.value = value.toInt()
                    else -> endYear.value = value.toInt()
                }
            },
            onSubcategoryClicked = { categoryType: CategoryType, subcategory: Subcategory ->
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
            },
            toolbarClickListener = object : ToolbarViewModel.ClickListener {
                override fun onConfirmClicked() {
                    super.onConfirmClicked()
                    appConfig.toolbarConfig.setBackFragment()
                    navController.goTo(
                        action = MovieGridFragmentAction(
                            MovieListType.Remote.Discover(
                                startYear = startYear.value.toString()
                                    .takeUnless { startYear.value == INITIAL_START_YEAR_VALUE },
                                endYear = endYear.value.toString(),
                                genreKeys = genreSubcategory.map { it.code },
                                languageKeys = languageSubcategory.map { it.code }
                            )
                        ),
                        animation = NavigationController.Animation.FromRight
                    )
                }
            }
        )
    }

    private fun MutableList<Subcategory>.addSubcategory(subcategory: Subcategory) {
        if (!contains(subcategory))
            add(subcategory)
    }
}