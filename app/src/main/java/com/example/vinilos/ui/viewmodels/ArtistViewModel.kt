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
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.repositories.BandRepository
import com.example.vinilos.data.repositories.MusicianRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

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
        loadArtists(ArtistType.MUSICIAN)
    }


    fun loadArtists(type: ArtistType) {
        viewModelScope.launch {
            try {
                val artistList = when (type) {
                    ArtistType.MUSICIAN -> musicianRepository.refreshData()
                    ArtistType.BAND -> bandRepository.refreshData()
                }
                _artists.postValue(artistList)
                _eventNetworkError.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    enum class ArtistType {
        MUSICIAN, BAND
    }


    fun getArtistById(id: Int, type: ArtistType): LiveData<Artist?> {
        val result = MediatorLiveData<Artist?>()

        if (_artists.value.isNullOrEmpty()) {
            loadArtists(type)
        }

        result.addSource(_artists) { artists ->
            result.value = artists?.find { it.id == id }
        }

        return result
    }

    fun formatDate(dateString: String?): String {
        Log.d("DateFormat", "Received date: $dateString")

        return try {

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("MMMM dd 'de' yyyy", Locale("es", "CO"))

            val date = dateString?.let { inputFormat.parse(it) }
            val formattedDate = outputFormat.format(date!!)

            val formattedDateWithCapitalMonth = formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            formattedDateWithCapitalMonth
        } catch (e: Exception) {

            "Fecha inválida"
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