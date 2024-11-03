package com.example.vinilos.data.models

data class Artist(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val creationDate: String,
    val albums: List<Album>,
    val type: ArtistType
) {
    enum class ArtistType {
        BAND,
        MUSICIAN
    }
}
