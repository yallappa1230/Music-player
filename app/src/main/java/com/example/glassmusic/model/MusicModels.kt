package com.example.glassmusic.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val artworkUrl: String,
    val streamUrl: String,
    val isAppleMusicCatalog: Boolean = false,
    val appleMusicId: String? = null,
    val genre: String = "Glass Pop",
    val releaseYear: String = "2024",
    val lyrics: String? = null
)

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val artworkUrl: String,
    val releaseYear: String,
    val trackCount: Int,
    val genre: String
)

data class Playlist(
    val id: String,
    val title: String,
    val description: String,
    val curator: String,
    val artworkUrl: String,
    val tracks: List<Song> = emptyList()
)

data class MusicCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val gradientStartHex: Long,
    val gradientEndHex: Long
)
