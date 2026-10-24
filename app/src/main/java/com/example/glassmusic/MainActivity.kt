package com.example.glassmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.glassmusic.service.PlaybackState
import com.example.glassmusic.ui.components.GlassBottomPlayer
import com.example.glassmusic.ui.components.GlassNavigationBar
import com.example.glassmusic.ui.screens.AppleMusicSettingsScreen
import com.example.glassmusic.ui.screens.BrowseScreen
import com.example.glassmusic.ui.screens.HomeScreen
import com.example.glassmusic.ui.screens.LibraryScreen
import com.example.glassmusic.ui.screens.NowPlayingSheet
import com.example.glassmusic.ui.screens.SearchScreen
import com.example.glassmusic.ui.theme.GlassDarkBg
import com.example.glassmusic.ui.theme.GlassMusicTheme
import com.example.glassmusic.ui.theme.liquidAmbientGlow
import com.example.glassmusic.viewmodel.MusicViewModel
import com.example.glassmusic.viewmodel.NavScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GlassMusicTheme(darkTheme = true) {
                GlassMusicApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GlassMusicApp(viewModel: MusicViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isNowPlayingExpanded by viewModel.isNowPlayingExpanded.collectAsStateWithLifecycle()
    val currentSong by viewModel.currentSong.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val currentPositionMs by viewModel.currentPositionMs.collectAsStateWithLifecycle()
    val durationMs by viewModel.durationMs.collectAsStateWithLifecycle()
    val isShuffle by viewModel.isShuffleEnabled.collectAsStateWithLifecycle()
    val repeatMode by viewModel.repeatMode.collectAsStateWithLifecycle()
    val likedSongIds by viewModel.likedSongIds.collectAsStateWithLifecycle()
    val queue by viewModel.queue.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val diagnostics by viewModel.diagnostics.collectAsStateWithLifecycle()

    // Back handling: if on secondary screen, go back to HOME
    if (currentScreen != NavScreen.HOME && !isNowPlayingExpanded) {
        BackHandler {
            viewModel.selectScreen(NavScreen.HOME)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassDarkBg)
            .liquidAmbientGlow()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Main Screen Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                NavScreen.HOME -> HomeScreen(
                    curatedSongs = viewModel.repository.curatedSongs,
                    curatedAlbums = viewModel.curatedAlbums,
                    currentSong = currentSong,
                    isPlaying = playbackState == PlaybackState.PLAYING,
                    diagnostics = diagnostics,
                    likedSongIds = likedSongIds,
                    onSongSelected = { song, trackList -> viewModel.playSong(song, trackList) },
                    onToggleLike = { id -> viewModel.toggleLike(id) },
                    onOpenSettings = { viewModel.selectScreen(NavScreen.SETTINGS) }
                )
                NavScreen.BROWSE -> BrowseScreen(
                    categories = viewModel.curatedCategories,
                    albums = viewModel.curatedAlbums,
                    curatedSongs = viewModel.repository.curatedSongs,
                    onSongSelected = { song, trackList -> viewModel.playSong(song, trackList) }
                )
                NavScreen.SEARCH -> SearchScreen(
                    searchQuery = searchQuery,
                    searchResults = searchResults,
                    isSearching = isSearching,
                    hasDeveloperToken = diagnostics.hasDeveloperToken,
                    onSearchQueryChanged = { query -> viewModel.onSearchQueryChanged(query) },
                    onSongSelected = { song, trackList -> viewModel.playSong(song, trackList) }
                )
                NavScreen.LIBRARY -> LibraryScreen(
                    curatedSongs = viewModel.repository.curatedSongs,
                    likedSongIds = likedSongIds,
                    currentSong = currentSong,
                    onSongSelected = { song, trackList -> viewModel.playSong(song, trackList) },
                    onToggleLike = { id -> viewModel.toggleLike(id) }
                )
                NavScreen.SETTINGS -> AppleMusicSettingsScreen(
                    diagnostics = diagnostics,
                    onSaveDeveloperToken = { token -> viewModel.saveAppleDeveloperToken(token) },
                    onSaveUserToken = { token -> viewModel.saveAppleUserToken(token) },
                    onSaveStorefront = { code -> viewModel.saveStorefront(code) },
                    onRefreshDiagnostics = { viewModel.refreshDiagnostics() }
                )
            }
        }

        // Floating Bottom Controls: Mini-Player + Navigation Bar
        Column(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            GlassBottomPlayer(
                song = currentSong,
                playbackState = playbackState,
                currentPositionMs = currentPositionMs,
                durationMs = durationMs,
                onExpandNowPlaying = { viewModel.setNowPlayingExpanded(true) },
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onSkipNext = { viewModel.skipNext() }
            )

            GlassNavigationBar(
                currentScreen = currentScreen,
                onScreenSelected = { screen -> viewModel.selectScreen(screen) }
            )
        }

        // Full Screen Liquid Glass Now Playing Modal
        AnimatedVisibility(
            visible = isNowPlayingExpanded,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            NowPlayingSheet(
                song = currentSong,
                playbackState = playbackState,
                currentPositionMs = currentPositionMs,
                durationMs = durationMs,
                isShuffle = isShuffle,
                repeatMode = repeatMode,
                isLiked = currentSong?.let { likedSongIds.contains(it.id) } ?: false,
                queue = queue,
                onDismiss = { viewModel.setNowPlayingExpanded(false) },
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onSeekTo = { pos -> viewModel.seekTo(pos) },
                onSkipNext = { viewModel.skipNext() },
                onSkipPrevious = { viewModel.skipPrevious() },
                onToggleShuffle = { viewModel.toggleShuffle() },
                onToggleRepeat = { viewModel.toggleRepeat() },
                onToggleLike = { id -> viewModel.toggleLike(id) },
                onSelectQueueSong = { song -> viewModel.playSong(song) }
            )
        }
    }
}
