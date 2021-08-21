package com.example.mmdb.navigation.configproviders

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.mmdb.config.AppConfig
import com.example.mmdb.config.requireAppConfig
import com.example.mmdb.navigation.NavigationController
import com.example.mmdb.navigation.requireNavController
import com.jokubas.mmdb.moviediscover.databinding.DiscoverAppBarContentViewBinding
import com.jokubas.mmdb.moviediscover.databinding.DiscoverToolbarToolsBinding
import com.jokubas.mmdb.moviediscover.model.entities.CategoryType
import com.jokubas.mmdb.moviediscover.model.entities.Subcategory
import com.jokubas.mmdb.moviediscover.model.repositories.CategoryRepository
import com.jokubas.mmdb.moviediscover.ui.appbar.DiscoverAppBarContentViewModel
import com.jokubas.mmdb.moviediscover.ui.appbar.DiscoverToolbarToolsViewModel
import com.jokubas.mmdb.moviediscover.ui.discover.DiscoverFragmentConfig
import com.jokubas.mmdb.moviegrid.actions.MovieGridFragmentAction
import com.jokubas.mmdb.moviegrid.actions.MovieListType
import com.jokubas.mmdb.util.navigationtools.ConfigProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
                fragment.context?.let { context ->
                    DiscoverToolbarToolsBinding.inflate(LayoutInflater.from(context)).apply {
                        viewModel = DiscoverToolbarToolsViewModel(
                            onConfirmClicked = {
                                appConfig.toolbarConfig.setBackFragment()
                                val startYearString = startYear.value.toString()
                                    .takeUnless { startYear.value == INITIAL_START_YEAR_VALUE }
                                navController.goTo(
                                    action = MovieGridFragmentAction(
                                        MovieListType.Remote.Discover(
                                            startYear = startYearString,
                                            endYear = endYear.value.toString(),
                                            genres = checkedGenres.map { it.code },
                                            languages = checkedLanguages.map { it.code },
                                            discoverNameList = listOfNotNull(
                                                startYearString?.let { "From: $startYearString" },
                                                "To: ${endYear.value}"
                                            )
                                                .plus(checkedGenres.map { it.name })
                                                .plus(checkedLanguages.map { it.name })
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