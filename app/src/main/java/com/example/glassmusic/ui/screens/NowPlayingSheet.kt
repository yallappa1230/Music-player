package com.example.glassmusic.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.glassmusic.model.Song
import com.example.glassmusic.service.PlaybackState
import com.example.glassmusic.service.RepeatMode
import com.example.glassmusic.ui.theme.GlassCyan
import com.example.glassmusic.ui.theme.GlassDarkBg
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import com.example.glassmusic.ui.theme.GlassDarkTextSecondary
import com.example.glassmusic.ui.theme.GlassRose
import com.example.glassmusic.ui.theme.GlassViolet
import com.example.glassmusic.ui.theme.glassPanel
import com.example.glassmusic.ui.theme.liquidAmbientGlow

@Composable
fun NowPlayingSheet(
    song: Song?,
    playbackState: PlaybackState,
    currentPositionMs: Long,
    durationMs: Long,
    isShuffle: Boolean,
    repeatMode: RepeatMode,
    isLiked: Boolean,
    queue: List<Song>,
    onDismiss: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleLike: (String) -> Unit,
    onSelectQueueSong: (Song) -> Unit,
    modifier: Modifier = Modifier
) {
    if (song == null) return

    BackHandler { onDismiss() }

    var isScrubbing by remember { mutableStateOf(false) }
    var scrubPosition by remember { mutableFloatStateOf(0f) }
    var showQueueDrawer by remember { mutableStateOf(false) }
    var showLyricsDrawer by remember { mutableStateOf(false) }

    val activePosition = if (isScrubbing) scrubPosition.toLong() else currentPositionMs
    val totalDuration = durationMs.coerceAtLeast(1000L)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GlassDarkBg)
            .liquidAmbientGlow(primaryColor = GlassCyan, secondaryColor = GlassViolet)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("now_playing_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                        .testTag("dismiss_now_playing_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Dismiss",
                        tint = GlassDarkTextPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "PLAYING FROM PLAYLIST",
                        color = GlassDarkTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = song.album,
                        color = GlassDarkTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { showQueueDrawer = !showQueueDrawer },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (showQueueDrawer) Color(0x4438BDF8) else Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Filled.QueueMusic,
                        contentDescription = "Queue",
                        tint = if (showQueueDrawer) GlassCyan else GlassDarkTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Frosted Artwork
            Box(
                modifier = Modifier
                    .size(290.dp)
                    .glassPanel(cornerRadius = 28.dp, bgAlpha = 0.25f, borderAlpha = 0.45f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = song.artworkUrl,
                    contentDescription = "Artwork for ${song.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(22.dp))
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Title & Artist & Like Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song.title,
                        color = GlassDarkTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = song.artist,
                        color = GlassDarkTextSecondary,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { onToggleLike(song.id) },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isLiked) GlassRose else GlassDarkTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Audio Quality Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .glassPanel(cornerRadius = 12.dp, bgAlpha = 0.15f, borderAlpha = 0.25f)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.GraphicEq,
                    contentDescription = null,
                    tint = GlassCyan,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Lossless • 24-bit / 48 kHz Apple ALAC",
                    color = GlassCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Glass Scrubber
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = activePosition.toFloat(),
                    onValueChange = {
                        isScrubbing = true
                        scrubPosition = it
                    },
                    onValueChangeFinished = {
                        isScrubbing = false
                        onSeekTo(scrubPosition.toLong())
                    },
                    valueRange = 0f..totalDuration.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = GlassDarkTextPrimary,
                        activeTrackColor = GlassCyan,
                        inactiveTrackColor = Color(0x33FFFFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(activePosition),
                        color = GlassDarkTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "-${formatTime(totalDuration - activePosition)}",
                        color = GlassDarkTextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Playback Control Cluster
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle
                IconButton(
                    onClick = onToggleShuffle,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffle) GlassCyan else GlassDarkTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Previous
                IconButton(
                    onClick = onSkipPrevious,
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Previous",
                        tint = GlassDarkTextPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Play / Pause glowing orb
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(GlassCyan, GlassViolet)
                            )
                        )
                        .border(2.dp, Color(0x66FFFFFF), CircleShape)
                        .clickable(onClick = onTogglePlayPause)
                        .testTag("now_playing_toggle_play_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (playbackState == PlaybackState.BUFFERING) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Icon(
                            imageVector = if (playbackState == PlaybackState.PLAYING) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (playbackState == PlaybackState.PLAYING) "Pause" else "Play",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Next
                IconButton(
                    onClick = onSkipNext,
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next",
                        tint = GlassDarkTextPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Repeat Mode
                IconButton(
                    onClick = onToggleRepeat,
                    modifier = Modifier.size(46.dp)
                ) {
                    val icon = if (repeatMode == RepeatMode.ONE) Icons.Filled.RepeatOne else Icons.Filled.Repeat
                    Icon(
                        imageVector = icon,
                        contentDescription = "Repeat",
                        tint = if (repeatMode != RepeatMode.OFF) GlassCyan else GlassDarkTextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom drawer toggles (Lyrics & Audio route)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .glassPanel(cornerRadius = 16.dp, bgAlpha = 0.16f, borderAlpha = 0.28f)
                        .clickable { showLyricsDrawer = !showLyricsDrawer }
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (showLyricsDrawer) "Hide Lyrics" else "View Lyrics",
                        color = if (showLyricsDrawer) GlassCyan else GlassDarkTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .glassPanel(cornerRadius = 16.dp, bgAlpha = 0.16f, borderAlpha = 0.28f)
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Airplay,
                            contentDescription = "Device Route",
                            tint = GlassDarkTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Phone Speaker",
                            color = GlassDarkTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Lyrics Box
            AnimatedVisibility(visible = showLyricsDrawer, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp)
                        .glassPanel(cornerRadius = 20.dp, bgAlpha = 0.20f, borderAlpha = 0.35f)
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "LYRICS",
                            color = GlassCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = song.lyrics ?: "Lyrics are unavailable for this track.",
                            color = GlassDarkTextPrimary,
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Queue Drawer
            AnimatedVisibility(visible = showQueueDrawer, enter = fadeIn(), exit = fadeOut()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 28.dp)
                        .glassPanel(cornerRadius = 20.dp, bgAlpha = 0.20f, borderAlpha = 0.35f)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "UP NEXT IN QUEUE",
                            color = GlassCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        queue.forEach { qSong ->
                            val isCurrent = qSong.id == song.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isCurrent) Color(0x3338BDF8) else Color.Transparent)
                                    .clickable { onSelectQueueSong(qSong) }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = qSong.artworkUrl,
                                    contentDescription = qSong.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = qSong.title,
                                        color = if (isCurrent) GlassCyan else GlassDarkTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = qSong.artist,
                                        color = GlassDarkTextMuted,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
