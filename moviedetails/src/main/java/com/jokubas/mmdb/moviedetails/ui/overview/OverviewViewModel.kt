package com.jokubas.mmdb.moviedetails.ui.overview

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.jokubas.mmdb.feedback_ui.LoadingViewModel
import com.jokubas.mmdb.feedback_ui.error.ErrorViewModel
import com.jokubas.mmdb.feedback_ui.error.GenericErrorViewModels
import com.jokubas.mmdb.model.data.entities.Movie
import com.jokubas.mmdb.moviedetails.BR
import com.jokubas.mmdb.moviedetails.R
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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