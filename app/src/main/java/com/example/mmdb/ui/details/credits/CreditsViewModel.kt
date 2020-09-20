package com.example.mmdb.ui.details.credits

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import com.example.mmdb.BR
import com.example.mmdb.R
import com.example.mmdb.util.managers.ProgressManager
import com.jokubas.mmdb.model.data.dataclasses.Credits
import com.jokubas.mmdb.model.data.dataclasses.Person
import com.jokubas.mmdb.model.remote.repositories.RemoteMovieRepository
import com.jokubas.mmdb.model.room.repositories.RoomMovieRepository
import com.jokubas.mmdb.util.DEFAULT_ID_VALUE
import com.jokubas.mmdb.util.isNetworkAvailable
import com.jokubas.mmdb.util.networkUnavailableNotification
import me.tatarka.bindingcollectionadapter2.ItemBinding

class CreditsViewModel(application: Application, movieLocalId: Int, movieRemoteId: Int) :
    AndroidViewModel(application) {

    val progressManager = ProgressManager().apply { loading() }

    private val credits =
        when (movieLocalId) {
            DEFAULT_ID_VALUE -> {
                if (isNetworkAvailable(application)) {
                    RemoteMovieRepository()
                        .getCreditsFlow(movieRemoteId)
                        .asLiveData(viewModelScope.coroutineContext)
                } else {
                    networkUnavailableNotification(application)
                    progressManager.error()
                    null
                }
            }
            else -> {
                RoomMovieRepository(application)
                    .getCreditsFlowById(movieLocalId)
                    .asLiveData(viewModelScope.coroutineContext)
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
        credits?.observeForever(creditsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        credits?.removeObserver(creditsObserver)
    }
}