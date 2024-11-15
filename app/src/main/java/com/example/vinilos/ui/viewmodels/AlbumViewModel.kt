package com.example.vinilos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.repositories.AlbumRepository

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumRepository = AlbumRepository(application)

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        loadAlbums()
    }

    /**
     * Fetches and refreshes the album data.
     */
    fun loadAlbums() {
        albumRepository.refreshData({ albumList ->
            _albums.postValue(albumList)
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false
        }, {
            if (_albums.value.isNullOrEmpty()) {
                _eventNetworkError.value = true
            }
        })
    }

    /**
     * Marks the network error as shown.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    /**
     * Retrieves an album by its ID.
     */
    fun getAlbumById(id: Int): LiveData<Album?> {
        val result = MediatorLiveData<Album?>()
        result.addSource(_albums) { albums ->
            result.value = albums?.find { it.id == id }
        }
        return result
    }

    /**
     * Factory for constructing AlbumViewModel with application context.
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}
