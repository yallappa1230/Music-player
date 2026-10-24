package com.example.glassmusic.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.glassmusic.ui.theme.glassmorphicBlur
import com.example.glassmusic.ui.theme.glassmorphicSurface

enum class SearchCategoryFilter {
    ALL,
    SONGS,
    ALBUMS,
    SPATIAL
}

@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: List<Song>,
    isSearching: Boolean,
    hasDeveloperToken: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSongSelected: (Song, List<Song>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(SearchCategoryFilter.ALL) }
    val trendingTags = listOf("Spatial Glass", "Aura Glass", "Liquid Pop", "Midnight", "Cryo Chill", "Ambient")

    val filteredResults = remember(searchResults, selectedFilter) {
        when (selectedFilter) {
            SearchCategoryFilter.ALL -> searchResults
            SearchCategoryFilter.SONGS -> searchResults
            SearchCategoryFilter.ALBUMS -> searchResults.distinctBy { it.album }
            SearchCategoryFilter.SPATIAL -> searchResults.filter {
                it.genre.contains("Spatial", ignoreCase = true) || it.genre.contains("Ambient", ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("search_screen"),
        contentPadding = PaddingValues(bottom = 120.dp, top = 16.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Search",
                            color = GlassDarkTextPrimary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (hasDeveloperToken) "Apple Music Catalog & Spatial Library" else "Instant Liquid Glass Music Catalog",
                            color = GlassDarkTextMuted,
                            fontSize = 13.sp
                        )
                    }

                    // Live catalog status badge
                    Box(
                        modifier = Modifier
                            .glassmorphicSurface(
                                shape = RoundedCornerShape(16.dp),
                                surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                                bgAlpha = 0.18f,
                                borderAlpha = 0.35f
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(if (hasDeveloperToken) GlassCyan else GlassViolet)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (hasDeveloperToken) "Apple Live" else "Spatial Catalog",
                                color = GlassDarkTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Glassmorphism Search Bar
        item {
            GlassmorphicSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChanged,
                onClear = { onSearchQueryChanged("") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        // Trending Quick Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                items(trendingTags) { tag ->
                    val isSelected = searchQuery.equals(tag, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .glassmorphicSurface(
                                shape = RoundedCornerShape(16.dp),
                                surfaceColor = if (isSelected) GlassCyan else MaterialTheme.colorScheme.surfaceVariant,
                                bgAlpha = if (isSelected) 0.35f else 0.14f,
                                borderAlpha = if (isSelected) 0.60f else 0.22f
                            )
                            .clickable { onSearchQueryChanged(tag) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tag,
                            color = if (isSelected) Color.White else GlassDarkTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Category Filter Tabs (All / Songs / Albums / Spatial)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchCategoryFilter.values().forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .glassmorphicSurface(
                                shape = RoundedCornerShape(12.dp),
                                surfaceColor = if (isSelected) GlassViolet else MaterialTheme.colorScheme.surface,
                                bgAlpha = if (isSelected) 0.30f else 0.10f,
                                borderAlpha = if (isSelected) 0.50f else 0.18f
                            )
                            .clickable { selectedFilter = filter }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter.name.lowercase().replaceFirstChar { it.uppercase() },
                            color = if (isSelected) GlassDarkTextPrimary else GlassDarkTextMuted,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Search Status Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (searchQuery.isBlank()) "Recommended Catalog" else "Results for \"$searchQuery\"",
                    color = GlassDarkTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = GlassCyan,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "${filteredResults.size} tracks",
                        color = GlassDarkTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Top Result Hero Spotlight (when query is active and results available)
        if (searchQuery.isNotBlank() && filteredResults.isNotEmpty()) {
            val topSong = filteredResults.first()
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                        .glassmorphicBlur(blurRadius = 14.dp)
                        .glassmorphicSurface(
                            shape = RoundedCornerShape(22.dp),
                            surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                            bgAlpha = 0.22f,
                            borderAlpha = 0.40f
                        )
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    GlassCyan.copy(alpha = 0.18f),
                                    GlassViolet.copy(alpha = 0.12f)
                                )
                            )
                        )
                        .clickable { onSongSelected(topSong, filteredResults) }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = topSong.artworkUrl,
                            contentDescription = topSong.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "TOP MATCH",
                                    color = GlassCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0x33FFFFFF))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = topSong.genre,
                                        color = GlassDarkTextPrimary,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = topSong.title,
                                color = GlassDarkTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${topSong.artist} • ${topSong.album}",
                                color = GlassDarkTextSecondary,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GlassCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play Top Match",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Empty State
        if (filteredResults.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 32.dp)
                        .glassmorphicSurface(
                            shape = RoundedCornerShape(20.dp),
                            surfaceColor = MaterialTheme.colorScheme.surface,
                            bgAlpha = 0.12f,
                            borderAlpha = 0.25f
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = GlassCyan,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No catalog results found",
                            color = GlassDarkTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Try searching for artists, songs, or spatial keywords like 'Aura Glass', 'Midnight Frost', or 'Synth'.",
                            color = GlassDarkTextMuted,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        } else {
            // Catalog Results List
            items(filteredResults) { song ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .glassmorphicSurface(
                            shape = RoundedCornerShape(16.dp),
                            surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                            bgAlpha = 0.12f,
                            borderAlpha = 0.22f
                        )
                        .clickable { onSongSelected(song, filteredResults) }
                        .padding(12.dp)
                        .testTag("search_result_item_${song.id}")
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

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x22FFFFFF)),
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
                }
            }
        }
    }
}

/**
 * Dedicated Glassmorphism Search Bar Composable with frosted backdrop,
 * specular edge lighting, and clear action.
 */
@Composable
fun GlassmorphicSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .glassmorphicBlur(blurRadius = 14.dp)
            .glassmorphicSurface(
                shape = RoundedCornerShape(22.dp),
                surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                bgAlpha = 0.18f,
                borderAlpha = 0.35f
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Search songs, artists, albums, or lyrics...",
                    color = GlassDarkTextMuted,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = GlassCyan,
                    modifier = Modifier.size(22.dp)
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x1AFFFFFF))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = GlassDarkTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = GlassDarkTextPrimary,
                unfocusedTextColor = GlassDarkTextPrimary,
                cursorColor = GlassCyan
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field")
        )
    }
}
