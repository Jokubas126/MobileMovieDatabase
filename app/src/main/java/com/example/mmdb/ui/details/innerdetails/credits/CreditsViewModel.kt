package com.example.mmdb.ui.details.innerdetails.credits

import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.managers.ProgressManager
import com.example.mmdb.navigation.actions.InnerDetailsAction
import com.jokubas.mmdb.model.data.entities.Credits
import com.jokubas.mmdb.model.data.entities.Person
import com.jokubas.mmdb.util.DataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.bindingcollectionadapter2.ItemBinding

class CreditsViewModel(
    action: InnerDetailsAction.Credits,
    config: CreditsConfig
) : ViewModel() {

    val progressManager = ProgressManager().apply { loading() }

    private val creditsObserver: (response: DataResponse) -> Unit = { response ->
        when {
            response is DataResponse.Success<*> && (response.value as? Credits?) != null -> {
                val credits = response.value as Credits?
                _castList.postValue(credits?.castList ?: emptyList())
                _crewList.postValue(credits?.crewList ?: emptyList())
                progressManager.success()
            }
            response is DataResponse.Error || response is DataResponse.Success<*> && (response.value as? Credits?) == null -> {
                progressManager.error()
            }
            else -> progressManager.loading()
        }
    }

    private val _castList: MutableLiveData<List<Person?>> = MutableLiveData(emptyList())
    val castList: LiveData<List<Person?>>
        get() = _castList

    private val _crewList: MutableLiveData<List<Person?>> = MutableLiveData(emptyList())
    val crewList: LiveData<List<Person?>>
        get() = _crewList

    private val response: LiveData<DataResponse> =
        config.provideCreditsDataFlow.invoke(action.movieIdWrapper).asLiveData(Dispatchers.IO)
            .apply { observeForever(creditsObserver) }

    val creditsItemBinding: ItemBinding<Person> = ItemBinding.of(BR.person, R.layout.item_person)

    override fun onCleared() {
        super.onCleared()
        response.removeObserver(creditsObserver)
    }

}