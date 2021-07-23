package com.example.mmdb.navigation.configproviders

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.databinding.DiscoverAppBarContentViewBinding
import com.example.mmdb.databinding.DiscoverToolbarToolsBinding
import com.example.mmdb.extensions.requireAppConfig
import com.example.mmdb.extensions.requireNavController
import com.example.mmdb.navigation.ConfigProvider
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.actions.MovieGridFragmentAction
import com.example.mmdb.navigation.actions.MovieListType
import com.example.mmdb.ui.ToolbarViewModel
import com.example.mmdb.ui.discover.DiscoverAppBarContentViewModel
import com.example.mmdb.ui.discover.DiscoverFragmentConfig
import com.example.mmdb.ui.discover.DiscoverToolbarToolsViewModel
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

    private var checkedLanguages = mutableListOf<Subcategory>()
    private var checkedGenres = mutableListOf<Subcategory>()

    override fun config(fragment: Fragment): DiscoverFragmentConfig {

        val appConfig: AppConfig = fragment.requireAppConfig()
        val navController: NavigationController = fragment.requireNavController()

        val categoryRepository: CategoryRepository = appConfig.movieConfig.categoryRepository

        return DiscoverFragmentConfig(
            provideCategories = {
                categoryRepository.categories()
            },
            onSubcategoryClicked = { categoryType: CategoryType, subcategory: Subcategory ->
                if (subcategory.isChecked) {
                    when (categoryType) {
                        CategoryType.LANGUAGES -> checkedLanguages.addSubcategory(subcategory)
                        CategoryType.GENRES -> checkedGenres.addSubcategory(subcategory)
                    }
                } else {
                    when (categoryType) {
                        CategoryType.LANGUAGES -> checkedLanguages.remove(subcategory)
                        CategoryType.GENRES -> checkedGenres.remove(subcategory)
                    }
                }
            },
            onBackClicked = {
                navController.goBack()
            },
            provideToolbarContent = { coroutineScope ->
                fragment.context?.let {
                    DiscoverAppBarContentViewBinding.inflate(
                        LayoutInflater.from(it)
                    ).apply {
                        viewModel = DiscoverAppBarContentViewModel(
                            coroutineScope = coroutineScope,
                            startYearFlow = startYear as StateFlow<Int>,
                            endYearFlow = endYear as StateFlow<Int>,
                            onRangeSliderValueChangedListener = { slider, value, _ ->
                                when (slider.activeThumbIndex == 0) {
                                    true -> startYear.value = value.toInt()
                                    else -> endYear.value = value.toInt()
                                }
                            }
                        )
                    }.root
                }
            },
            provideToolbarToolsView = {
                fragment.context?.let {
                    DiscoverToolbarToolsBinding.inflate(
                        LayoutInflater.from(it)
                    ).apply {
                        viewModel = DiscoverToolbarToolsViewModel(
                            onConfirmClicked = {
                                appConfig.toolbarConfig.setBackFragment()
                                navController.goTo(
                                    action = MovieGridFragmentAction(
                                        MovieListType.Remote.Discover(
                                            startYear = startYear.value.toString()
                                                .takeUnless { startYear.value == INITIAL_START_YEAR_VALUE },
                                            endYear = endYear.value.toString(),
                                            genres = checkedGenres,
                                            languages = checkedLanguages
                                        )
                                    ),
                                    animation = NavigationController.Animation.FromRight
                                )
                            }
                        )
                    }.root

                }
            }
        )
    }

    private fun MutableList<Subcategory>.addSubcategory(subcategory: Subcategory) {
        if (!contains(subcategory))
            add(subcategory)
    }
}