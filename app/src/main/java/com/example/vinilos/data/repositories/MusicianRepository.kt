package com.example.vinilos.data.repositories

import android.content.Context
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.network.NetworkServiceAdapter

class MusicianRepository(context: Context) {

    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    suspend fun refreshData(): List<Artist> {
        return networkServiceAdapter.getMusicians()
    }
}