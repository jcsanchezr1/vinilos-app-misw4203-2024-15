package com.example.vinilos.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.repositories.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel(application: Application) : AndroidViewModel(application) {

    private val collectorRepository = CollectorRepository(application)

    private val _collectors = MutableLiveData<List<Collector>>()
    val collectors: LiveData<List<Collector>> get() = _collectors

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        loadCollectors()
    }

    private fun loadCollectors() {
        viewModelScope.launch {
            try {
                val collectorList = collectorRepository.getCollectors()
                _collectors.postValue(collectorList)
                _eventNetworkError.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun getCollectorById(id: Int): LiveData<Collector?> {
        val result = MediatorLiveData<Collector?>()
        result.addSource(_collectors) { collectors ->
            result.value = collectors?.find { it.id == id }
        }
        return result
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
