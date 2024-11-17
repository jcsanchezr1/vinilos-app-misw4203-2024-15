package com.example.vinilos.data.repositories

import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.network.NetworkServiceAdapter
import android.content.Context

class BandRepository(context: Context) {

    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    suspend fun refreshData(): List<Artist> {
        return networkServiceAdapter.getBands()
    }
}

