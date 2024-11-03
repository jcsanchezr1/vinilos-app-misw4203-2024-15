package com.example.vinilos.data.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.network.NetworkServiceAdapter

class MusicianRepository(val application: Application) {
    fun refreshData(callback: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getMusicians({ callback(it) }, onError)
    }
}