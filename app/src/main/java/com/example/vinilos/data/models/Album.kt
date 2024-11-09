package com.example.vinilos.data.models

import java.io.Serializable

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val releaseDate: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track>,
    val performers: List<Artist>,
    val comments: List<Comment>,
) : Serializable
