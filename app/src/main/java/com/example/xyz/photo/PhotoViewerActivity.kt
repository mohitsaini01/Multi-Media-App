package com.example.xyz.photo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.xyz.R

class PhotoViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_photo_viewer)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        // Get the photo paths from the intent (assuming you pass a list of photo paths)
        val photoPaths = intent.getStringArrayListExtra("photo_paths") ?: arrayListOf()

        if (photoPaths.isNotEmpty()) {
            // Create an adapter and set it to ViewPager2
            val photoViewerAdapter = PhotoViewerAdapter(photoPaths)
            viewPager.adapter = photoViewerAdapter

            // Optionally, you can set the current item to the selected image's position
            val currentPosition = intent.getIntExtra("photo_position", 0)
            viewPager.setCurrentItem(currentPosition)
        }
    }
}
