package com.example.glassmusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.glassmusic.api.AppleMusicDiagnostic
import com.example.glassmusic.model.Album
import com.example.glassmusic.model.Song
import com.example.glassmusic.ui.theme.GlassCyan
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import com.example.glassmusic.ui.theme.GlassDarkTextSecondary
import com.example.glassmusic.ui.theme.GlassRose
import com.example.glassmusic.ui.theme.GlassViolet
import com.example.glassmusic.ui.theme.glassPanel

@Composable
fun HomeScreen(
    curatedSongs: List<Song>,
    curatedAlbums: List<Album>,
    currentSong: Song?,
    isPlaying: Boolean,
    diagnostics: AppleMusicDiagnostic,
    likedSongIds: Set<String>,
    onSongSelected: (Song, List<Song>) -> Unit,
    onToggleLike: (String) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 120.dp, top = 16.dp)
    ) {
        // Header row with Apple Music status pill
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Listen Now",
                        color = GlassDarkTextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Liquid Glass Audio • Android 15",
                        color = GlassDarkTextMuted,
                        fontSize = 13.sp
                    )
                }

                // Apple Music Status badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .glassPanel(cornerRadius = 20.dp, bgAlpha = 0.2f, borderAlpha = 0.4f)
                        .clickable(onClick = onOpenSettings)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("apple_music_status_badge"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (diagnostics.hasDeveloperToken) GlassCyan else GlassRose)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (diagnostics.hasDeveloperToken) "Apple API Ready" else "Setup Apple Key",
                            color = GlassDarkTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Setup",
                            tint = GlassDarkTextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Hero Glass Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .glassPanel(cornerRadius = 24.dp, bgAlpha = 0.18f, borderAlpha = 0.35f)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                GlassCyan.copy(alpha = 0.15f),
                                GlassViolet.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GraphicEq,
                            contentDescription = null,
                            tint = GlassCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "SPATIAL AUDIO IN LIQUID GLASS",
                            color = GlassCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Acoustic Clarity,\nTranslucent Depth",
                        color = GlassDarkTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Experience 24-bit studio sound with seamless Apple Music catalog search and real streaming playback.",
                        color = GlassDarkTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Heavy Rotation Carousel
        item {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Heavy Rotation",
                    color = GlassDarkTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(curatedSongs) { song ->
                        val isCurrent = currentSong?.id == song.id
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .glassPanel(
                                    cornerRadius = 20.dp,
                                    bgAlpha = if (isCurrent) 0.3f else 0.14f,
                                    borderAlpha = if (isCurrent) 0.5f else 0.25f
                                )
                                .clickable { onSongSelected(song, curatedSongs) }
                                .padding(12.dp)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(136.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                ) {
                                    AsyncImage(
                                        model = song.artworkUrl,
                                        contentDescription = song.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    // Play icon overlay
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(8.dp)
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0x88000000))
                                            .border(1.dp, Color(0x66FFFFFF), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Play",
                                            tint = GlassCyan,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = song.title,
                                    color = GlassDarkTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = song.artist,
                                    color = GlassDarkTextMuted,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // Top Refractions
        item {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = "Top Tracks & Refractions",
                    color = GlassDarkTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }

        itemsIndexed(curatedSongs) { index, song ->
            val isCurrent = currentSong?.id == song.id
            val isLiked = likedSongIds.contains(song.id)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .glassPanel(
                        cornerRadius = 16.dp,
                        bgAlpha = if (isCurrent) 0.28f else 0.10f,
                        borderAlpha = if (isCurrent) 0.45f else 0.20f
                    )
                    .clickable { onSongSelected(song, curatedSongs) }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .testTag("track_item_${song.id}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        color = if (isCurrent) GlassCyan else GlassDarkTextMuted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(28.dp)
                    )

                    AsyncImage(
                        model = song.artworkUrl,
                        contentDescription = song.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            color = if (isCurrent) GlassCyan else GlassDarkTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${song.artist} • ${song.genre}",
                            color = GlassDarkTextMuted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = { onToggleLike(song.id) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isLiked) "Unlike" else "Like",
                            tint = if (isLiked) GlassRose else GlassDarkTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
