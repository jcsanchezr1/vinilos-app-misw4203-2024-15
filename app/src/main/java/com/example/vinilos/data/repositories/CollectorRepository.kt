package com.example.vinilos.data.repositories

import android.content.Context
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.network.NetworkServiceAdapter

class CollectorRepository(context: Context) {

    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    suspend fun getCollectors(): List<Collector> {
        return networkServiceAdapter.getCollectors()
    }
}
