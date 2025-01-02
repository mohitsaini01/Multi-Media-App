package com.example.xyz.video

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.xyz.R

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_player)


        val playButton: ImageView = findViewById(R.id.imageView)
        val pauseButton: ImageView = findViewById(R.id.imageView2)
        val videoView:VideoView =findViewById(R.id.videoview)


        val videoPath = intent.getStringExtra("video_path")

        if (videoPath != null) {
            val uri = Uri.parse(videoPath)
            videoView.setVideoURI(uri)
        }

        playButton.setOnClickListener {
            if (!videoView.isPlaying) {
                videoView.start()
            }
        }

        pauseButton.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
            }
        }



    }
}
