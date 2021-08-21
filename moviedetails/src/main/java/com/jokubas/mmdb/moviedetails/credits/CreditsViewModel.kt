package com.jokubas.mmdb.moviedetails.credits

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.jokubas.mmdb.feedback_ui.LoadingViewModel
import com.jokubas.mmdb.feedback_ui.error.ErrorViewModel
import com.jokubas.mmdb.feedback_ui.error.GenericErrorViewModels
import com.jokubas.mmdb.moviedetails.BR
import com.jokubas.mmdb.moviedetails.R
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class CreditsViewModel(
    private val action: InnerDetailsAction.Credits,
    private val config: CreditsConfig
) : ViewModel() {

    private val networkErrorViewModel = GenericErrorViewModels.NetworkErrorViewModel {
        loadMovieInfo()
    }

    val item: ObservableField<Any> = ObservableField(LoadingViewModel)

    val itemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(LoadingViewModel::class.java, BR.viewModel, R.layout.loading_view)
        .map(ErrorViewModel::class.java, BR.viewModel, R.layout.error_view)
        .map(CreditsContentViewModel::class.java, BR.viewModel, R.layout.movie_credits_content)

    init {
        loadMovieInfo()
    }

    private fun loadMovieInfo() {
        item.set(LoadingViewModel)
        viewModelScope.launch(Dispatchers.IO) {
            config.provideCreditsDataFlow.invoke(action.isRemote, action.movieId).collect { response ->
                when {
                    response.body() != null -> {
                        val credits = response.body()
                        item.set(
                            CreditsContentViewModel(
                                castList = credits?.castList ?: emptyList(),
                                crewList = credits?.crewList ?: emptyList()
                            )
                        )
                    }
                    response is DataResponse.Error -> {
                        item.set(networkErrorViewModel)
                    }
                    response is DataResponse.Empty -> {
                        item.set(GenericErrorViewModels.EmptyViewModel)
                    }
                    else -> item.set(LoadingViewModel)
                }
            }
        }
    }

}