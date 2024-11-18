package com.example.vinilos.data.repositories

import android.content.Context
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.network.NetworkServiceAdapter

class AlbumRepository(context: Context) {

    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    suspend fun getAlbums(): List<Album> {
        return networkServiceAdapter.getAlbums()
    }
}

