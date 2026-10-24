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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.glassmusic.model.Song
import com.example.glassmusic.ui.theme.GlassCyan
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import com.example.glassmusic.ui.theme.GlassDarkTextSecondary
import com.example.glassmusic.ui.theme.GlassRose
import com.example.glassmusic.ui.theme.GlassViolet
import com.example.glassmusic.ui.theme.glassPanel

@Composable
fun LibraryScreen(
    curatedSongs: List<Song>,
    likedSongIds: Set<String>,
    currentSong: Song?,
    onSongSelected: (Song, List<Song>) -> Unit,
    onToggleLike: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val likedSongs = curatedSongs.filter { likedSongIds.contains(it.id) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("library_screen"),
        contentPadding = PaddingValues(bottom = 120.dp, top = 16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(
                    text = "Library",
                    color = GlassDarkTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Saved Songs, Favorites & Custom Glass Collections",
                    color = GlassDarkTextMuted,
                    fontSize = 13.sp
                )
            }
        }

        // Favorites Hero Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .glassPanel(cornerRadius = 24.dp, bgAlpha = 0.20f, borderAlpha = 0.38f)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                GlassRose.copy(alpha = 0.20f),
                                GlassViolet.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = null,
                                    tint = GlassRose,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Favorite Songs",
                                    color = GlassDarkTextPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${likedSongs.size} loved tracks saved",
                                color = GlassDarkTextSecondary,
                                fontSize = 13.sp
                            )
                        }

                        if (likedSongs.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(GlassCyan)
                                    .clickable { onSongSelected(likedSongs.first(), likedSongs) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Play all favorites",
                                    tint = Color.Black,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Stats row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .glassPanel(cornerRadius = 18.dp, bgAlpha = 0.12f, borderAlpha = 0.20f)
                        .padding(14.dp)
                ) {
                    Column {
                        Text(text = "Playlists", color = GlassDarkTextMuted, fontSize = 12.sp)
                        Text(text = "4 Curated", color = GlassDarkTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .glassPanel(cornerRadius = 18.dp, bgAlpha = 0.12f, borderAlpha = 0.20f)
                        .padding(14.dp)
                ) {
                    Column {
                        Text(text = "Audio Cache", color = GlassDarkTextMuted, fontSize = 12.sp)
                        Text(text = "Online Stream", color = GlassCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = if (likedSongs.isEmpty()) "Suggested to Favorite" else "Favorite Tracks",
                color = GlassDarkTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
            )
        }

        val displaySongs = if (likedSongs.isNotEmpty()) likedSongs else curatedSongs

        items(displaySongs) { song ->
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
                    .clickable { onSongSelected(song, displaySongs) }
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = song.artworkUrl,
                        contentDescription = song.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = song.title,
                            color = if (isCurrent) GlassCyan else GlassDarkTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${song.artist} • ${song.album}",
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
