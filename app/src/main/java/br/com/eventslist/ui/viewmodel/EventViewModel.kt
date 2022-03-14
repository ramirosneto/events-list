package br.com.eventslist.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.eventslist.data.model.EventCheckInRequest
import br.com.eventslist.data.api.EventRepository
import br.com.eventslist.utils.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventViewModel(private val repository: EventRepository) : ViewModel() {

    val progressLiveStatus = MutableLiveData<NetworkStatus>()

    fun getEvents() {
        viewModelScope.launch {
            progressLiveStatus.postValue(NetworkStatus.Loading)
            withContext(Dispatchers.IO) {
                try {
                    val result = repository.getEvents()
                    progressLiveStatus.postValue(NetworkStatus.Success(result))
                } catch (throwable: Throwable) {
                    progressLiveStatus.postValue(NetworkStatus.Error(throwable.message.toString()))
                }
            }
        }
    }

    fun getEventDetail(id: String) {
        viewModelScope.launch {
            progressLiveStatus.postValue(NetworkStatus.Loading)
            withContext(Dispatchers.IO) {
                try {
                    val result = repository.getEventDetail(id)
                    progressLiveStatus.postValue(NetworkStatus.Success(result))
                } catch (throwable: Throwable) {
                    progressLiveStatus.postValue(NetworkStatus.Error(throwable.message.toString()))
                }
            }
        }
    }

    fun checkIn(request: EventCheckInRequest) {
        viewModelScope.launch {
            progressLiveStatus.postValue(NetworkStatus.Loading)
            withContext(Dispatchers.IO) {
                try {
                    val result = repository.checkIn(request)
                    progressLiveStatus.postValue(NetworkStatus.Success(result))
                } catch (throwable: Throwable) {
                    progressLiveStatus.postValue(NetworkStatus.Error(throwable.message.toString()))
                }
            }
        }
    }
}
