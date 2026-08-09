package com.example

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Song(
    val uri: String,
    val title: String,
    val duration: String
)

object MusicPlayerManager {
    private var prefs: SharedPreferences? = null
    
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _isRepeat = MutableStateFlow(false)
    val isRepeat: StateFlow<Boolean> = _isRepeat.asStateFlow()
    
    private var mediaPlayer: MediaPlayer? = null
    
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
            val uri = prefs?.getString("song_uri", null)
            val title = prefs?.getString("song_title", null)
            val duration = prefs?.getString("song_duration", null)
            
            if (uri != null && title != null && duration != null) {
                _currentSong.value = Song(uri, title, duration)
            }
        }
    }
    
    fun playSong(context: Context, song: Song) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context.applicationContext, Uri.parse(song.uri))
                prepare()
                isLooping = _isRepeat.value
                start()
            }
            _currentSong.value = song
            _isPlaying.value = true
            
            prefs?.edit()
                ?.putString("song_uri", song.uri)
                ?.putString("song_title", song.title)
                ?.putString("song_duration", song.duration)
                ?.apply()
                
            mediaPlayer?.setOnCompletionListener {
                if (!_isRepeat.value) {
                    _isPlaying.value = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isPlaying.value = false
        }
    }
    
    fun togglePlayPause(context: Context) {
        val player = mediaPlayer
        if (player != null) {
            if (player.isPlaying) {
                player.pause()
                _isPlaying.value = false
            } else {
                player.start()
                _isPlaying.value = true
            }
        } else {
            _currentSong.value?.let { playSong(context, it) }
        }
    }
    
    fun stop() {
        mediaPlayer?.pause()
        mediaPlayer?.seekTo(0)
        _isPlaying.value = false
    }
    
    fun toggleRepeat() {
        _isRepeat.value = !_isRepeat.value
        mediaPlayer?.isLooping = _isRepeat.value
    }
    
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
    }
}
