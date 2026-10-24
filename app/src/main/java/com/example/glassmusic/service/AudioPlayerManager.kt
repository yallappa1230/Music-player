package com.example.glassmusic.service

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.glassmusic.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class PlaybackState {
    IDLE,
    BUFFERING,
    PLAYING,
    PAUSED,
    ERROR
}

enum class RepeatMode {
    OFF,
    ALL,
    ONE
}

class AudioPlayerManager(private val context: Context) {

    private val tag = "AudioPlayerManager"
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.ALL)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _isShuffleEnabled = MutableStateFlow(false)
    val isShuffleEnabled: StateFlow<Boolean> = _isShuffleEnabled.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    var onTrackCompleted: (() -> Unit)? = null

    private fun initMediaPlayer(): MediaPlayer {
        mediaPlayer?.release()
        val player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener { mp ->
                _playbackState.value = PlaybackState.PLAYING
                _durationMs.value = mp.duration.toLong().coerceAtLeast(0L)
                mp.start()
                startProgressTracker()
            }
            setOnBufferingUpdateListener { _, percent ->
                Log.d(tag, "Buffering: $percent%")
            }
            setOnCompletionListener {
                if (_repeatMode.value == RepeatMode.ONE) {
                    seekTo(0)
                    play()
                } else {
                    _playbackState.value = PlaybackState.PAUSED
                    onTrackCompleted?.invoke()
                }
            }
            setOnErrorListener { _, what, extra ->
                Log.e(tag, "MediaPlayer error: what=$what, extra=$extra")
                _playbackState.value = PlaybackState.ERROR
                _errorMessage.value = "Playback error ($what, $extra)"
                false
            }
        }
        mediaPlayer = player
        return player
    }

    fun playSong(song: Song) {
        try {
            stopProgressTracker()
            _errorMessage.value = null
            _playbackState.value = PlaybackState.BUFFERING
            _currentPositionMs.value = 0L
            _durationMs.value = song.durationMs

            val player = initMediaPlayer()
            player.setDataSource(song.streamUrl)
            player.prepareAsync()
        } catch (e: Exception) {
            Log.e(tag, "Error loading stream: ${e.message}", e)
            _playbackState.value = PlaybackState.ERROR
            _errorMessage.value = "Could not stream audio: ${e.localizedMessage}"
        }
    }

    fun play() {
        mediaPlayer?.let { player ->
            if (!player.isPlaying) {
                player.start()
                _playbackState.value = PlaybackState.PLAYING
                startProgressTracker()
            }
        }
    }

    fun pause() {
        mediaPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
                _playbackState.value = PlaybackState.PAUSED
                stopProgressTracker()
            }
        }
    }

    fun togglePlayPause() {
        if (_playbackState.value == PlaybackState.PLAYING) {
            pause()
        } else if (_playbackState.value == PlaybackState.PAUSED) {
            play()
        }
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.let { player ->
            try {
                player.seekTo(positionMs.toInt())
                _currentPositionMs.value = positionMs
            } catch (e: Exception) {
                Log.e(tag, "Seek error: ${e.message}")
            }
        }
    }

    fun toggleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
    }

    fun toggleShuffle() {
        _isShuffleEnabled.value = !_isShuffleEnabled.value
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        _currentPositionMs.value = player.currentPosition.toLong()
                        if (player.duration > 0) {
                            _durationMs.value = player.duration.toLong()
                        }
                    }
                }
                delay(250)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        stopProgressTracker()
        mediaPlayer?.release()
        mediaPlayer = null
        _playbackState.value = PlaybackState.IDLE
    }
}
