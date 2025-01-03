package com.example.xyz.music

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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

    private var musicPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        playButton = findViewById(R.id.play_button)
        pauseButton = findViewById(R.id.pause_button)
        stopButton = findViewById(R.id.stop_button)
        musicTitle = findViewById(R.id.music_title)
        seekBar = findViewById(R.id.seekBar)

        musicPath = intent.getStringExtra("music_path")
        if (musicPath != null) {
            musicTitle.text = musicPath!!.substringAfterLast("/") // Show file name
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
}
