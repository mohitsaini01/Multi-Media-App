package com.example.xyz.music

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.xyz.R

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageView
    private lateinit var pauseButton: ImageView
    private lateinit var stopButton: ImageView
    private lateinit var musicTitle: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var musicIcon: ImageView
    private lateinit var musicNext: ImageView
    private lateinit var musicPrev: ImageView

    private var musicPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        stopButton = findViewById(R.id.stop_button)
        musicTitle = findViewById(R.id.music_title)
        seekBar = findViewById(R.id.seekBar)
        musicIcon = findViewById(R.id.imageView4)
        musicNext = findViewById(R.id.next_button)
        musicPrev = findViewById(R.id.previous_button)

        val defaultIcon = R.drawable.music // Add default icon
        musicIcon.setImageResource(defaultIcon)

        musicPath = intent.getStringExtra("music_path")
        if (musicPath != null) {
            musicTitle.text = musicPath!!.substringAfterLast("/") // Show file name

            val albumArt = getAlbumArt(musicPath!!)
            if (albumArt != null) {
                musicIcon.setImageBitmap(albumArt) // Set album art if available
            } else {
                musicIcon.setImageResource(defaultIcon) // Set default icon if no album art
            }
        }

        playButton.setOnClickListener {
            startMusicService("PLAY")
        }

        pauseButton.setOnClickListener {
            startMusicService("PAUSE")
        }

        stopButton.setOnClickListener {
            startMusicService("STOP")
        }
        musicNext.setOnClickListener {
            startMusicService("NEXT")
        }
        musicPrev.setOnClickListener{
            startMusicService("PREVIOUS")
        }

    }

    private fun startMusicService(action: String) {
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("action", action) // Pass the action (PLAY, PAUSE, STOP)
        intent.putExtra("music_path", musicPath)
        startService(intent) // Start or control the service
    }

    private fun stopMusicService() {
        val intent = Intent(this, MusicService::class.java)
        stopService(intent) // Stop the service
    }

    private fun getAlbumArt(songPath: String): Bitmap? {
        val albumArt: Bitmap? = try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(songPath)
            val art = mediaMetadataRetriever.embeddedPicture
            art?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        } catch (e: Exception) {
            null
        }
        return albumArt
    }
}