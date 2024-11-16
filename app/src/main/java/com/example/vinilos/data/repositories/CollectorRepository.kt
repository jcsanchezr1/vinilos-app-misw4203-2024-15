package com.example.vinilos.data.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.network.NetworkServiceAdapter

class CollectorRepository(val application: Application) {
    fun getCollectors(callback: (List<Collector>) -> Unit, onError: (VolleyError) -> Unit) {
        NetworkServiceAdapter.getInstance(application).getCollectors({ callback(it) }, onError)
    }
}
