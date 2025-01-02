package com.example.xyz.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false // Track whether music is paused or playing

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("action") // To differentiate between play, pause, etc.
        val musicPath = intent?.getStringExtra("music_path")

        when (action) {
            "PLAY" -> {
                if (this::mediaPlayer.isInitialized && isPaused) {
                    // Resume if already paused
                    mediaPlayer.start()
                    isPaused = false
                } else if (musicPath != null) {
                    if (this::mediaPlayer.isInitialized) {
                        mediaPlayer.stop()
                        mediaPlayer.reset()
                    }

                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(musicPath)
                        prepare()
                        start()
                    }
                } else {
                    Log.e("MusicService", "Music path is null")
                }
            }

            "PAUSE" -> {
                if (this::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    isPaused = true
                }
            }

            "STOP" -> {
                if (this::mediaPlayer.isInitialized) {
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                }
            }

            else -> Log.e("MusicService", "Unknown action: $action")
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
