package com.example.xyz.music

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xyz.R

class MusicFragment : Fragment() {

    private lateinit var musicRecyclerView: RecyclerView
    private lateinit var musicAdapter: MusicAdapter
    private val musicList = mutableListOf<MusicItem>() // Add your music titles here


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        musicRecyclerView = view.findViewById(R.id.rv2)
        musicRecyclerView.layoutManager = LinearLayoutManager(context)

        loadMusic()
        // musicAdapter = MusicAdapter(musicList)

        musicAdapter = MusicAdapter(musicList) { musicItem ->
            openMusicPlayer(musicItem)
        }



        musicRecyclerView.adapter = musicAdapter

        return view
    }

    private fun loadMusic() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )

        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val path = it.getString(dataColumn)
                musicList.add(MusicItem(title, artist, path))
            }
        }
    }


    data class MusicItem(val title: String, val artist: String, val path: String)


    private fun openMusicPlayer(musicItem: MusicItem) {
        val intent = Intent(context, MusicPlayerActivity::class.java)
        intent.putExtra("music_path", musicItem.path) // Ensure the correct path is passed
        context?.startActivity(intent)
    }


}


