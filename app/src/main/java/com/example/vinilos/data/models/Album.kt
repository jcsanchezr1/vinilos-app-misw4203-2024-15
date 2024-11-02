package com.example.vinilos.data.models

import com.example.vinilos.data.models.Artist.ArtistType

data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val releaseDate: String,
    val genre: String,
    val recordLabel: String,
    val tracks:List<Track>,
    val performers:List<Artist>,
    val comments:List<Comment>,
)
