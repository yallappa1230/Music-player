package com.example.glassmusic.data

import android.content.Context
import android.util.Log
import com.example.glassmusic.api.AppleMusicApiClient
import com.example.glassmusic.api.AppleMusicKitBridge
import com.example.glassmusic.model.Album
import com.example.glassmusic.model.MusicCategory
import com.example.glassmusic.model.Playlist
import com.example.glassmusic.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class MusicRepository(
    private val context: Context,
    private val appleMusicBridge: AppleMusicKitBridge
) {
    private val tag = "MusicRepository"
    private val prefs = context.getSharedPreferences("glass_music_library", Context.MODE_PRIVATE)

    private val _likedSongIds = MutableStateFlow<Set<String>>(emptySet())
    val likedSongIds: StateFlow<Set<String>> = _likedSongIds.asStateFlow()

    init {
        loadLikedSongs()
    }

    private fun loadLikedSongs() {
        val saved = prefs.getStringSet("liked_song_ids", emptySet()) ?: emptySet()
        _likedSongIds.value = saved
    }

    fun toggleLikeSong(songId: String) {
        val current = _likedSongIds.value.toMutableSet()
        if (current.contains(songId)) {
            current.remove(songId)
        } else {
            current.add(songId)
        }
        prefs.edit().putStringSet("liked_song_ids", current).apply()
        _likedSongIds.value = current
    }

    // High fidelity curated tracks with real streaming audio endpoints
    val curatedSongs: List<Song> = listOf(
        Song(
            id = "glass_1",
            title = "Liquid Refraction",
            artist = "Aura Glass",
            album = "Prism Horizons",
            durationMs = 195000L,
            artworkUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&q=80",
            streamUrl = "https://actions.google.com/sounds/v1/water/rain_heavy.ogg",
            genre = "Spatial Ambient",
            releaseYear = "2024",
            lyrics = "Walking through the glass facade\nReflections of another world\nWhere the light bends into neon\nLiquid silence, clear and bold\nDrifting in the prism sky."
        ),
        Song(
            id = "glass_2",
            title = "Midnight Frost",
            artist = "Cryo Velvet",
            album = "Translucent Dreams",
            durationMs = 212000L,
            artworkUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=600&q=80",
            streamUrl = "https://actions.google.com/sounds/v1/weather/wind_strong_cold.ogg",
            genre = "Chillwave",
            releaseYear = "2024",
            lyrics = "Subzero frequencies falling like frost\nTouch the panel and lose what was lost\nCold glass, warm heart\nNever fading from the start."
        ),
        Song(
            id = "glass_3",
            title = "Prism Symphony",
            artist = "Obsidian Ray",
            album = "Lucid Architecture",
            durationMs = 184000L,
            artworkUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&q=80",
            streamUrl = "https://actions.google.com/sounds/v1/ambiences/outdoor_ambience_spring.ogg",
            genre = "Modern Classical",
            releaseYear = "2024",
            lyrics = "Notes suspended in crystalline spheres\nEvery octave conquering years\nGlass strings resonate high\nInto the infinite sky."
        ),
        Song(
            id = "glass_4",
            title = "Solar Flare Glow",
            artist = "Neon Mirage",
            album = "Reflected City",
            durationMs = 228000L,
            artworkUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=600&q=80",
            streamUrl = "https://actions.google.com/sounds/v1/water/waves_crashing.ogg",
            genre = "Synth Pop",
            releaseYear = "2023",
            lyrics = "Golden hour through tinted shades\nEvery worry gently fades\nLiquid glass under our feet\nDancing down the avenue street."
        ),
        Song(
            id = "glass_5",
            title = "Crystalline Echoes",
            artist = "Vapour Void",
            album = "Aether State",
            durationMs = 205000L,
            artworkUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&q=80",
            streamUrl = "https://actions.google.com/sounds/v1/science_fiction/force_field_hum.ogg",
            genre = "Electronic",
            releaseYear = "2024",
            lyrics = "Echoes bounce off polished walls\nAnswer whenever the future calls\nBreathe in the digital air\nNo other rhythm can compare."
        ),
        Song(
            id = "glass_6",
            title = "Aurora Borealis",
            artist = "Starlight Ensemble",
            album = "Northern Glow",
            durationMs = 240000L,
            artworkUrl = "https://images.unsplash.com/photo-1517411032315-54ef2cb783bb?w=600&q=80",
            streamUrl = "https://actions.google.com/sounds/v1/ambiences/campfire.ogg",
            genre = "Ambient Chill",
            releaseYear = "2024",
            lyrics = "Ribbons of cyan across the night\nPure luminescence, unfiltered light\nFrozen glass whispers so low\nWatching the Arctic aurora glow."
        )
    )

    val curatedAlbums: List<Album> = listOf(
        Album(
            id = "alb_1",
            title = "Prism Horizons",
            artist = "Aura Glass",
            artworkUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&q=80",
            releaseYear = "2024",
            trackCount = 12,
            genre = "Spatial Ambient"
        ),
        Album(
            id = "alb_2",
            title = "Translucent Dreams",
            artist = "Cryo Velvet",
            artworkUrl = "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?w=600&q=80",
            releaseYear = "2024",
            trackCount = 10,
            genre = "Chillwave"
        ),
        Album(
            id = "alb_3",
            title = "Lucid Architecture",
            artist = "Obsidian Ray",
            artworkUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&q=80",
            releaseYear = "2024",
            trackCount = 8,
            genre = "Modern Classical"
        ),
        Album(
            id = "alb_4",
            title = "Aether State",
            artist = "Vapour Void",
            artworkUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&q=80",
            releaseYear = "2024",
            trackCount = 14,
            genre = "Electronic"
        )
    )

    val curatedCategories: List<MusicCategory> = listOf(
        MusicCategory(
            id = "cat_spatial",
            title = "Spatial Glass",
            subtitle = "Dolby Atmos & 3D Refractions",
            iconName = "Headphones",
            gradientStartHex = 0xFF0284C7,
            gradientEndHex = 0xFF38BDF8
        ),
        MusicCategory(
            id = "cat_pop",
            title = "Liquid Pop",
            subtitle = "Chart Toppers & Neon Melodies",
            iconName = "MusicNote",
            gradientStartHex = 0xFFD946EF,
            gradientEndHex = 0xFF818CF8
        ),
        MusicCategory(
            id = "cat_chill",
            title = "Frosted Chill",
            subtitle = "Lo-Fi Beats & Relaxing Hues",
            iconName = "Nightlight",
            gradientStartHex = 0xFF0D9488,
            gradientEndHex = 0xFF2DD4BF
        ),
        MusicCategory(
            id = "cat_electronic",
            title = "Cyber Synth",
            subtitle = "Upbeat Basslines & Glass Drops",
            iconName = "Bolt",
            gradientStartHex = 0xFF6366F1,
            gradientEndHex = 0xFFA855F7
        ),
        MusicCategory(
            id = "cat_classical",
            title = "Acoustic Crystal",
            subtitle = "Strings, Grand Piano & Reverb",
            iconName = "Piano",
            gradientStartHex = 0xFFF59E0B,
            gradientEndHex = 0xFFFBBF24
        ),
        MusicCategory(
            id = "cat_hiphop",
            title = "Urban Rhythm",
            subtitle = "808 Low Ends & Crisp Highs",
            iconName = "Equalizer",
            gradientStartHex = 0xFFE11D48,
            gradientEndHex = 0xFFFB7185
        )
    )

    suspend fun searchTracks(query: String): List<Song> = withContext(Dispatchers.IO) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return@withContext curatedSongs

        val devToken = appleMusicBridge.getDeveloperToken()
        if (devToken.isNotBlank()) {
            try {
                val bearer = if (devToken.startsWith("Bearer ")) devToken else "Bearer $devToken"
                val storefront = appleMusicBridge.getStorefront()
                val userToken = appleMusicBridge.getUserToken().takeIf { it.isNotBlank() }

                val response = AppleMusicApiClient.api.searchCatalog(
                    authorization = bearer,
                    userToken = userToken,
                    storefront = storefront,
                    term = trimmed
                )

                val songsData = response.results?.songs?.data ?: emptyList()
                if (songsData.isNotEmpty()) {
                    return@withContext songsData.map { item ->
                        val attr = item.attributes
                        val previewUrl = attr?.previews?.firstOrNull()?.url ?: ""
                        Song(
                            id = item.id,
                            title = attr?.name ?: "Unknown Title",
                            artist = attr?.artistName ?: "Unknown Artist",
                            album = attr?.albumName ?: "Apple Music Single",
                            durationMs = attr?.durationInMillis ?: 180000L,
                            artworkUrl = attr?.artwork?.formatUrl(600)
                                ?: "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&q=80",
                            streamUrl = previewUrl.ifEmpty { curatedSongs.first().streamUrl },
                            isAppleMusicCatalog = true,
                            appleMusicId = item.id,
                            genre = attr?.genreNames?.firstOrNull() ?: "Apple Music",
                            releaseYear = attr?.releaseDate?.take(4) ?: "2024"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w(tag, "Apple Music live search failed, falling back to local: ${e.message}")
            }
        }

        // Search local curated library
        curatedSongs.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
                    it.artist.contains(trimmed, ignoreCase = true) ||
                    it.album.contains(trimmed, ignoreCase = true) ||
                    it.genre.contains(trimmed, ignoreCase = true)
        }
    }
}
