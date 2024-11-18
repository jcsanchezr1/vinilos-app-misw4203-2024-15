package com.example.vinilos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.repositories.BandRepository
import com.example.vinilos.data.repositories.MusicianRepository
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private val bandRepository = BandRepository(application)

    private val musicianRepository = MusicianRepository(application)

    private val _artists = MutableLiveData<List<Artist>>()
    val artists: LiveData<List<Artist>> get() = _artists

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        loadMusicians()
    }

    fun loadBands() {
        viewModelScope.launch {
            try {
                val artistList = bandRepository.refreshData()
                _artists.postValue(artistList)
                _eventNetworkError.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun loadMusicians() {
        viewModelScope.launch {
            try {
                val artistList = musicianRepository.refreshData()
                _artists.postValue(artistList)
                _eventNetworkError.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}