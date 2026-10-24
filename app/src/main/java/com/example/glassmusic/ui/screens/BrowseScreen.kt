package com.example.glassmusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.glassmusic.model.Album
import com.example.glassmusic.model.MusicCategory
import com.example.glassmusic.model.Song
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import com.example.glassmusic.ui.theme.GlassDarkTextSecondary
import com.example.glassmusic.ui.theme.glassPanel

@Composable
fun BrowseScreen(
    categories: List<MusicCategory>,
    albums: List<Album>,
    curatedSongs: List<Song>,
    onSongSelected: (Song, List<Song>) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("browse_screen"),
        contentPadding = PaddingValues(bottom = 120.dp, top = 16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(
                    text = "Browse",
                    color = GlassDarkTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Explore Stations, Genres & Liquid Spatial Audio",
                    color = GlassDarkTextMuted,
                    fontSize = 13.sp
                )
            }
        }

        // Category Cards Section
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = "Featured Refraction Genres",
                    color = GlassDarkTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 2-column category items
                categories.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { cat ->
                            CategoryCard(
                                category = cat,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        // Pick a song matching the category or first song
                                        val match = curatedSongs.firstOrNull {
                                            it.genre.contains(cat.title.split(" ").last(), ignoreCase = true)
                                        } ?: curatedSongs.first()
                                        onSongSelected(match, curatedSongs)
                                    }
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Albums Spotlight
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Liquid Glass Albums",
                    color = GlassDarkTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                albums.forEach { album ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .glassPanel(cornerRadius = 18.dp, bgAlpha = 0.12f, borderAlpha = 0.22f)
                            .clickable {
                                val match = curatedSongs.firstOrNull { it.album == album.title }
                                    ?: curatedSongs.first()
                                onSongSelected(match, curatedSongs)
                            }
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = album.artworkUrl,
                                contentDescription = album.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Column(
                                modifier = Modifier
                                    .padding(start = 14.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = album.title,
                                    color = GlassDarkTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${album.artist} • ${album.genre}",
                                    color = GlassDarkTextSecondary,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${album.trackCount} Tracks • ${album.releaseYear}",
                                    color = GlassDarkTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: MusicCategory,
    modifier: Modifier = Modifier
) {
    val icon = when (category.iconName) {
        "Headphones" -> Icons.Filled.Headphones
        "Nightlight" -> Icons.Filled.Nightlight
        "Bolt" -> Icons.Filled.Bolt
        "Piano" -> Icons.Filled.Piano
        "Equalizer" -> Icons.Filled.Equalizer
        else -> Icons.Filled.MusicNote
    }

    Box(
        modifier = modifier
            .height(100.dp)
            .glassPanel(cornerRadius = 20.dp, bgAlpha = 0.16f, borderAlpha = 0.32f)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(category.gradientStartHex).copy(alpha = 0.30f),
                        Color(category.gradientEndHex).copy(alpha = 0.12f)
                    )
                )
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(category.gradientStartHex),
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = category.title,
                    color = GlassDarkTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = category.subtitle,
                    color = GlassDarkTextMuted,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
