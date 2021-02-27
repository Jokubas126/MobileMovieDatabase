package com.example.mmdb.ui.details.innerdetails.credits

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.config.AppConfig
import com.example.mmdb.managers.ProgressManager
import com.jokubas.mmdb.model.data.entities.Credits
import com.jokubas.mmdb.model.data.entities.Person
import com.jokubas.mmdb.util.DEFAULT_ID_VALUE
import me.tatarka.bindingcollectionadapter2.ItemBinding

class CreditsViewModel(
    appConfig: AppConfig,
    movieLocalId: Int,
    movieRemoteId: Int
) : ViewModel() {

    val progressManager = ProgressManager().apply { loading() }

    private val credits =
        when (movieLocalId) {
            DEFAULT_ID_VALUE -> {
                if (appConfig.networkCheckConfig.isNetworkAvailable()) {
                    /*RemoteMovieRepository()
                        .getCreditsFlow(movieRemoteId)
                        .asLiveData(viewModelScope.coroutineContext)*/
                } else {
                    appConfig.networkCheckConfig.networkUnavailableNotification()
                    progressManager.error()
                    null
                }
            }
            else -> {
                null
                //RoomMovieRepository(application)
                    //.getCreditsFlowById(movieLocalId)
                    //.asLiveData(viewModelScope.coroutineContext)
            }
        }

    val creditsItemBinding: ItemBinding<Person> = ItemBinding.of(BR.person, R.layout.item_person)

    val castItems: ObservableList<Person?> = ObservableArrayList()
    val crewItems: ObservableArrayList<Person?> = ObservableArrayList()

    private val creditsObserver: (credits: Credits?) -> Unit = { credits ->
        credits?.let {
            when {
                it.castList.isNullOrEmpty() && it.crewList.isNullOrEmpty() -> progressManager.error()
                else -> {
                    it.castList?.let { cast -> castItems.addAll(cast) }
                    it.crewList?.let { crew -> crewItems.addAll(crew) }
                    progressManager.loaded()
                }
            }
        } ?: progressManager.error()
    }

    init {
        //credits?.observeForever(creditsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        //credits?.removeObserver(creditsObserver)
    }
}