package com.example.mmdb.ui.details.innerdetails.overview

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.example.mmdb.ui.movielists.moviegrid.ItemMovieViewModel
import com.example.mmdb.ui.movielists.moviegrid.MovieGridContentViewModel
import com.jokubas.mmdb.feedback_ui.LoadingViewModel
import com.jokubas.mmdb.feedback_ui.error.ErrorViewModel
import com.jokubas.mmdb.feedback_ui.error.GenericErrorViewModels
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class OverviewViewModel(
    private val action: InnerDetailsAction.Overview,
    private val config: OverviewConfig
) : ViewModel() {

    private val networkErrorViewModel = GenericErrorViewModels.NetworkErrorViewModel {
        loadMovieInfo()
    }

    val item: ObservableField<Any> = ObservableField(LoadingViewModel)

    val itemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(LoadingViewModel::class.java, BR.viewModel, R.layout.loading_view)
        .map(ErrorViewModel::class.java, BR.viewModel, R.layout.error_view)
        .map(OverviewContentViewModel::class.java, BR.viewModel, R.layout.movie_overview_content)

    init {
        loadMovieInfo()
    }

    private fun loadMovieInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            config.provideOverviewInfo.invoke(action.movieId, action.isRemote).collect { response ->
                item.set(
                    when (response) {
                        is DataResponse.Success<Movie> -> {
                            response.body()?.let {
                                OverviewContentViewModel(it)
                            } ?: GenericErrorViewModels.EmptyViewModel
                        }
                        is DataResponse.Error -> networkErrorViewModel
                        is DataResponse.Loading -> LoadingViewModel
                        else -> GenericErrorViewModels.EmptyViewModel
                    }
                )
            }
        }
    }
}