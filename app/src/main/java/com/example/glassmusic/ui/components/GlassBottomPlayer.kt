package com.example.glassmusic.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.glassmusic.model.Song
import com.example.glassmusic.service.PlaybackState
import com.example.glassmusic.ui.theme.GlassCyan
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import androidx.compose.material3.MaterialTheme
import com.example.glassmusic.ui.theme.glassmorphicBlur
import com.example.glassmusic.ui.theme.glassmorphicSurface

@Composable
fun GlassBottomPlayer(
    song: Song?,
    playbackState: PlaybackState,
    currentPositionMs: Long,
    durationMs: Long,
    onExpandNowPlaying: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (song == null) return

    val progress = if (durationMs > 0) {
        (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .glassmorphicBlur(blurRadius = 16.dp)
            .glassmorphicSurface(
                shape = RoundedCornerShape(24.dp),
                surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                bgAlpha = 0.22f,
                borderAlpha = 0.35f
            )
            .clickable(onClick = onExpandNowPlaying)
            .testTag("mini_player_container")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album Art
                AsyncImage(
                    model = song.artworkUrl,
                    contentDescription = "Album art for ${song.title}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x33FFFFFF))
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Title and Artist
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = song.title,
                        color = GlassDarkTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${song.artist} • ${song.album}",
                        color = GlassDarkTextMuted,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Controls
                if (playbackState == PlaybackState.BUFFERING) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(28.dp)
                            .padding(4.dp),
                        color = GlassCyan,
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(
                        onClick = onTogglePlayPause,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x22FFFFFF))
                            .testTag("mini_play_pause_button")
                    ) {
                        Icon(
                            imageVector = if (playbackState == PlaybackState.PLAYING) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (playbackState == PlaybackState.PLAYING) "Pause" else "Play",
                            tint = GlassDarkTextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onSkipNext,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("mini_skip_next_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Skip Next",
                        tint = GlassDarkTextPrimary.copy(alpha = 0.85f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Glass progress bar along bottom edge
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp),
                color = GlassCyan,
                trackColor = Color(0x1AFFFFFF),
            )
        }
    }
}
