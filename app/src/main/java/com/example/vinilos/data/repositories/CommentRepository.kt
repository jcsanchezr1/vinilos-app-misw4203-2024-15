package com.example.vinilos.data.repositories

import android.content.Context
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.network.NetworkServiceAdapter

class CommentRepository(context: Context) {
    private val networkServiceAdapter = NetworkServiceAdapter.getInstance(context)

    suspend fun getComments(albumId: Int): List<Comment> {
        return networkServiceAdapter.getComments(albumId)
    }

    suspend fun postComment(albumId: Int, comment: Comment): Comment {
        return networkServiceAdapter.postComment(albumId, comment)
    }
}