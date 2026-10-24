package com.example.glassmusic.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.glassmusic.api.AppleMusicDiagnostic
import com.example.glassmusic.api.AppleMusicKitBridge
import com.example.glassmusic.data.MusicRepository
import com.example.glassmusic.model.Album
import com.example.glassmusic.model.MusicCategory
import com.example.glassmusic.model.Song
import com.example.glassmusic.service.AudioPlayerManager
import com.example.glassmusic.service.PlaybackState
import com.example.glassmusic.service.RepeatMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavScreen {
    HOME,
    BROWSE,
    SEARCH,
    LIBRARY,
    SETTINGS
}

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val appleMusicBridge = AppleMusicKitBridge(application)
    val repository = MusicRepository(application, appleMusicBridge)
    val audioPlayer = AudioPlayerManager(application)

    private val _currentScreen = MutableStateFlow(NavScreen.HOME)
    val currentScreen: StateFlow<NavScreen> = _currentScreen.asStateFlow()

    private val _isNowPlayingExpanded = MutableStateFlow(false)
    val isNowPlayingExpanded: StateFlow<Boolean> = _isNowPlayingExpanded.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>>(repository.curatedSongs)
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private val _queueIndex = MutableStateFlow(0)
    val queueIndex: StateFlow<Int> = _queueIndex.asStateFlow()

    val playbackState: StateFlow<PlaybackState> = audioPlayer.playbackState
    val currentPositionMs: StateFlow<Long> = audioPlayer.currentPositionMs
    val durationMs: StateFlow<Long> = audioPlayer.durationMs
    val isShuffleEnabled: StateFlow<Boolean> = audioPlayer.isShuffleEnabled
    val repeatMode: StateFlow<RepeatMode> = audioPlayer.repeatMode
    val errorMessage: StateFlow<String?> = audioPlayer.errorMessage

    val likedSongIds: StateFlow<Set<String>> = repository.likedSongIds

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Song>>(repository.curatedSongs)
    val searchResults: StateFlow<List<Song>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _diagnostics = MutableStateFlow(appleMusicBridge.getDiagnostics())
    val diagnostics: StateFlow<AppleMusicDiagnostic> = _diagnostics.asStateFlow()

    val curatedAlbums: List<Album> = repository.curatedAlbums
    val curatedCategories: List<MusicCategory> = repository.curatedCategories

    init {
        // Set first track ready on launch
        if (repository.curatedSongs.isNotEmpty()) {
            _currentSong.value = repository.curatedSongs[0]
            _queue.value = repository.curatedSongs
        }

        audioPlayer.onTrackCompleted = {
            skipNext()
        }
    }

    fun selectScreen(screen: NavScreen) {
        _currentScreen.value = screen
    }

    fun setNowPlayingExpanded(expanded: Boolean) {
        _isNowPlayingExpanded.value = expanded
    }

    fun playSong(song: Song, newQueue: List<Song>? = null) {
        if (newQueue != null && newQueue.isNotEmpty()) {
            _queue.value = newQueue
            val index = newQueue.indexOfFirst { it.id == song.id }
            _queueIndex.value = if (index >= 0) index else 0
        } else {
            val index = _queue.value.indexOfFirst { it.id == song.id }
            if (index >= 0) {
                _queueIndex.value = index
            } else {
                _queue.value = listOf(song) + _queue.value
                _queueIndex.value = 0
            }
        }

        _currentSong.value = song
        audioPlayer.playSong(song)
    }

    fun togglePlayPause() {
        if (_currentSong.value == null && _queue.value.isNotEmpty()) {
            playSong(_queue.value.first())
            return
        }
        audioPlayer.togglePlayPause()
    }

    fun seekTo(positionMs: Long) {
        audioPlayer.seekTo(positionMs)
    }

    fun skipNext() {
        val currentQueue = _queue.value
        if (currentQueue.isEmpty()) return

        val nextIndex = if (isShuffleEnabled.value) {
            (currentQueue.indices).random()
        } else {
            (_queueIndex.value + 1) % currentQueue.size
        }

        _queueIndex.value = nextIndex
        val nextSong = currentQueue[nextIndex]
        _currentSong.value = nextSong
        audioPlayer.playSong(nextSong)
    }

    fun skipPrevious() {
        val currentQueue = _queue.value
        if (currentQueue.isEmpty()) return

        // If played more than 3 seconds, restart current track
        if (currentPositionMs.value > 3000L) {
            seekTo(0)
            audioPlayer.play()
            return
        }

        val prevIndex = if (_queueIndex.value - 1 < 0) {
            currentQueue.size - 1
        } else {
            _queueIndex.value - 1
        }

        _queueIndex.value = prevIndex
        val prevSong = currentQueue[prevIndex]
        _currentSong.value = prevSong
        audioPlayer.playSong(prevSong)
    }

    fun toggleShuffle() {
        audioPlayer.toggleShuffle()
    }

    fun toggleRepeat() {
        audioPlayer.toggleRepeatMode()
    }

    fun toggleLike(songId: String) {
        repository.toggleLikeSong(songId)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _isSearching.value = true
            val results = repository.searchTracks(query)
            _searchResults.value = results
            _isSearching.value = false
        }
    }

    fun saveAppleDeveloperToken(token: String) {
        appleMusicBridge.setDeveloperToken(token)
        refreshDiagnostics()
    }

    fun saveAppleUserToken(token: String) {
        appleMusicBridge.setUserToken(token)
        refreshDiagnostics()
    }

    fun saveStorefront(storefront: String) {
        appleMusicBridge.setStorefront(storefront)
        refreshDiagnostics()
    }

    fun refreshDiagnostics() {
        _diagnostics.value = appleMusicBridge.getDiagnostics()
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
