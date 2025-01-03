package com.example.xyz.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xyz.R

class MusicAdapter(private val musicList: List<MusicFragment.MusicItem>, private val onClick:(MusicFragment.MusicItem) ->Unit) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicIcon: ImageView = itemView.findViewById(R.id.profile)
        val musicTitle: TextView = itemView.findViewById(R.id.name)
        val musicArtist: TextView = itemView.findViewById(R.id.artistname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_music, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val musicItem = musicList[position]

        holder.musicTitle.text = musicItem.title
        holder.musicArtist.text = musicItem.artist
        val albumArt = getAlbumArt(musicItem.path)
        if (albumArt != null) {
            holder.musicIcon.setImageBitmap(albumArt)  // Set album art
        } else {
            val defaultIcon = R.drawable.music // Default icon
            holder.musicIcon.setImageResource(defaultIcon)  // Set default icon if no album art
        }


        holder.itemView.setOnClickListener { onClick(musicItem) }

    }

    override fun getItemCount(): Int = musicList.size

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