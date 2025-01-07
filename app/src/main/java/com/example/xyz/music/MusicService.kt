package com.example.xyz.music

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.xyz.R
import android.provider.MediaStore
import android.database.Cursor
import android.graphics.BitmapFactory

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false
    private var currentSongIndex: Int = 0
    private lateinit var songList: List<String>

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
        loadSongsFromDevice()  // Load songs from device storage
        showMusicNotification("Song Name", "Artist Name")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("action")
        val musicPath = intent?.getStringExtra("music_path")

        when (action) {
            "PLAY" -> {
                if (this::mediaPlayer.isInitialized && isPaused) {
                    mediaPlayer.start()
                    isPaused = false
                } else if (musicPath != null) {
                    // Set currentSongIndex based on the passed song path
                    currentSongIndex = songList.indexOf(musicPath)

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

            "NEXT" -> {
                nextTrack()  // Proceed to next song in the list
            }

            "PREVIOUS" -> {
                previousTrack()  // Go to previous song in the list
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
        stopForeground(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun createNotificationChannel(context: Context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel_id",
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            putExtra("action", action)
        }
        return PendingIntent.getService(
            this, action.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun showMusicNotification(title: String, artist: String) {
        val previousIntent = getPendingIntent("PREVIOUS")
        val playIntent = getPendingIntent("PLAY")
        val pauseIntent = getPendingIntent("PAUSE")
        val nextIntent = getPendingIntent("NEXT")
        //val previousIntent = getPendingIntent("PREVIOUS")

        val notification = NotificationCompat.Builder(this, "music_channel_id")
            .setSmallIcon(R.drawable.music)
            .setContentTitle(title)
            .setContentText(artist)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .addAction(R.drawable.baseline_skip_previous_24, "Previous", previousIntent)
            .addAction(R.drawable.baseline_play_circle_24, "Play", playIntent)
            .addAction(R.drawable.baseline_pause_circle_24, "Pause", pauseIntent)
            .addAction(R.drawable.baseline_skip_next_24, "Next", nextIntent)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.background)) // Set background image as large icon
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1, 2, 3,))
            .build()

        startForeground(1, notification)
    }

    private fun loadSongsFromDevice() {
        val resolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = resolver.query(uri, projection, null, null, null)

        songList = mutableListOf()

        cursor?.let {
            while (it.moveToNext()) {
                val filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                songList = songList + filePath
            }
            it.close()
        }

        if (songList.isEmpty()) {
            Log.e("MusicService", "No songs found on device")
        }
    }

    private fun nextTrack() {
        if (songList.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songList.size  // Loop through songs
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.setDataSource(songList[currentSongIndex])
            mediaPlayer.prepare()
            mediaPlayer.start()
            showMusicNotification("Song Title", "Artist Name")  // Update notification with new song info
        }
    }

    private fun previousTrack() {
        if (songList.isNotEmpty()) {
            currentSongIndex = if (currentSongIndex - 1 < 0) songList.size - 1 else currentSongIndex - 1  // Loop to last song
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.setDataSource(songList[currentSongIndex])
            mediaPlayer.prepare()
            mediaPlayer.start()
            showMusicNotification("Song Title", "Artist Name")  // Update notification with new song info
        }
    }
}