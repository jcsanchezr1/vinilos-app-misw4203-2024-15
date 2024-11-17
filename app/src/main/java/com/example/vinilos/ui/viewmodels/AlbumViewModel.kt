package com.example.vinilos.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.repositories.AlbumRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumRepository = AlbumRepository(application)

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    private val _isTracksEmpty = MutableLiveData<Boolean>()
    val isTracksEmpty: LiveData<Boolean> get() = _isTracksEmpty

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            try {
                val albumList = albumRepository.getAlbums() // Calls the suspend function
                _albums.postValue(albumList) // Updates LiveData
                _eventNetworkError.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun getAlbumById(id: Int): LiveData<Album?> {
        val result = MediatorLiveData<Album?>()
        result.addSource(_albums) { albums ->
            result.value = albums?.find { it.id == id }
        }
        return result
    }

    fun setAlbum(album: Album) {
        _isTracksEmpty.value = album.tracks.isEmpty()
    }

    fun formatDateAndGenre(dateString: String?, genre: String?): String {
        return try {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("MMMM dd 'de' yyyy", Locale("es", "CO"))

            val date = dateString?.let { inputFormat.parse(it) }
            val formattedDate = outputFormat.format(date!!)

            val formattedDateWithCapitalMonth = formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            "$formattedDateWithCapitalMonth - ${genre ?: ""}"
        } catch (e: Exception) {
            "Fecha inválida - ${genre ?: ""}"
        }
    }

    fun postComment(albumId: Int, description: String, rating: Int) {
        viewModelScope.launch {
            try {
                val newComment = albumRepository.postComment(albumId, Comment(
                    0,
                    description,
                    rating,
                    100
                ))
                val updatedAlbums = _albums.value?.map { album ->
                    if (album.id == albumId) {
                        val updatedComments = album.comments + newComment
                        album.copy(comments = updatedComments)
                    } else {
                        album
                    }
                }
                _albums.postValue(updatedAlbums!!)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

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