package com.example.vinilos.data.cache

import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Comment

class CacheManager {
    companion object{
        private var instance: CacheManager? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CacheManager().also {
                    instance = it
                }
            }
    }
    private var comments: HashMap<Int, List<Comment>> = hashMapOf()
    private val albumCache: HashMap<Int, Album> = hashMapOf()

    fun addComments(albumId: Int, newComments: List<Comment>) {
        if (comments.containsKey(albumId)) {
            val existingComments = comments[albumId]?.toMutableList() ?: mutableListOf()
            existingComments.addAll(newComments)
            comments[albumId] = existingComments
        } else {
            comments[albumId] = newComments
        }
    }

    fun getComments(albumId: Int) : List<Comment>{
        return if (comments.containsKey(albumId)) comments[albumId]!! else listOf()
    }

    fun setAlbums(albums: List<Album>) {
        for (album in albums) {
            albumCache[album.id] = album
        }
    }

    fun addAlbum(album: Album) {
        albumCache[album.id] = album
    }

    fun getAlbums(): List<Album> {
        return albumCache.values.toList()
    }
}