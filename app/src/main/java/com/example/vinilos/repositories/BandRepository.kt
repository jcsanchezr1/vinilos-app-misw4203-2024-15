package com.example.vinilos.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter

class BandRepository(val application: Application) {
    fun refreshData(callback: (List<Artist>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getBands({ callback(it) }, onError)
    }
}