package com.hrithik.myscanner.room

import androidx.lifecycle.*
import com.hrithik.myscanner.model.QrHistoryInfo
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class QrInfoViewModel(private val repository: QrInfoRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allQrHistoryInfo : LiveData<MutableList<QrHistoryInfo>> = repository.allQrInfo.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(qrHistoryInfo: QrHistoryInfo) = viewModelScope.launch {
        repository.insert(qrHistoryInfo)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun deleteQrInfo(qrHistoryInfo: QrHistoryInfo) = viewModelScope.launch {
        repository.deleteQrInfo(qrHistoryInfo)
    }

    class QrInfoViewModelFactory(private val repository: QrInfoRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QrInfoViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return QrInfoViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown VieModel Class")
        }

    }
}