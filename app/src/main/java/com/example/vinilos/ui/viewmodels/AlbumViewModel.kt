package com.example.vinilos.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.repositories.AlbumRepository
import java.text.SimpleDateFormat
import java.util.Locale

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumRepository = AlbumRepository(application)

    private val _albums = MutableLiveData<List<Album>>()
    val albums: LiveData<List<Album>> get() = _albums

    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    private val _isTracksEmpty = MutableLiveData<Boolean>()
    val isTracksEmpty: LiveData<Boolean> get() = _isTracksEmpty

    init {
        loadAlbums()
    }

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
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")

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
        val newComment = Comment(
            id = 0,
            description = description,
            rating = rating,
            collector = 100
        )

        albumRepository.postComment(albumId, newComment, { createdComment ->
            // Find the album in the existing list and update its comments
            val updatedAlbums = _albums.value?.map { album ->
                if (album.id == albumId) {
                    val updatedComments = album.comments + createdComment
                    album.copy(comments = updatedComments)
                } else {
                    album
                }
            }

            _albums.postValue(updatedAlbums)
        }, { error ->
            Log.e("AlbumViewModel", "Error creando el comentario: ${error.message}")
            _eventNetworkError.postValue(true)
        })
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
