package com.jokubas.mmdb.moviedetails.media

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.jokubas.mmdb.feedback_ui.LoadingViewModel
import com.jokubas.mmdb.feedback_ui.error.ErrorViewModel
import com.jokubas.mmdb.feedback_ui.error.GenericErrorViewModels
import com.jokubas.mmdb.model.data.entities.filterMainTrailer
import com.jokubas.mmdb.moviedetails.BR
import com.jokubas.mmdb.moviedetails.R
import com.jokubas.mmdb.moviedetails.actions.InnerDetailsAction
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class MediaViewModel(
    private val action: InnerDetailsAction.Media,
    private val config: MediaConfig
) : ViewModel() {

    private val networkErrorViewModel = GenericErrorViewModels.NetworkErrorViewModel {
        loadMovieInfo()
    }

    val item: ObservableField<Any> = ObservableField(LoadingViewModel)

    val itemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(LoadingViewModel::class.java, BR.viewModel, R.layout.loading_view)
        .map(ErrorViewModel::class.java, BR.viewModel, R.layout.error_view)
        .map(MediaContentViewModel::class.java, BR.viewModel, R.layout.movie_media_content)

    private val mediaContentViewModel: MediaContentViewModel?
        get() = item.get() as? MediaContentViewModel

    init {
        loadMovieInfo()
    }

    private fun loadMovieInfo() {
        item.set(LoadingViewModel)
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                config.provideTrailer.invoke(action.movieId, action.isRemote),
                config.provideImages.invoke(action.movieId, action.isRemote)
            ) { trailerResponse, imagesResponse -> trailerResponse to imagesResponse }
                .collect { (trailerResponse, imagesResponse) ->
                    val mainTrailer = trailerResponse.body()?.filterMainTrailer()
                    when {
                        trailerResponse is DataResponse.Loading && imagesResponse is DataResponse.Loading -> {
                            item.set(LoadingViewModel)
                        }
                        trailerResponse is DataResponse.Error && imagesResponse is DataResponse.Error -> {
                            item.set(networkErrorViewModel)
                        }
                        mainTrailer == null && imagesResponse.body()?.backdropList?.isEmpty() == true
                                && imagesResponse.body()?.posterList?.isEmpty() == true -> {
                            item.set(GenericErrorViewModels.EmptyViewModel)
                        }
                        else -> {
                            mainTrailer?.let {
                                mediaContentViewModel?.updateTrailer(mainTrailer)
                                    ?: item.set(
                                        MediaContentViewModel(
                                            lifecycle = config.lifecycle,
                                            initialTrailer = mainTrailer
                                        )
                                    )
                            }
                            if (imagesResponse is DataResponse.Success<*>) {
                                imagesResponse.body()?.let {
                                    mediaContentViewModel?.updateImages(it)
                                        ?: item.set(
                                            MediaContentViewModel(
                                                lifecycle = config.lifecycle,
                                                initialImages = it
                                            )
                                        )
                                }
                            }
                        }
                    }
                }
        }
    }
}