package com.example.xyz.video

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xyz.R

class VideoAdapter(private val videoList: List<VideoFragment.VideoItem>, private val onClick: (VideoFragment.VideoItem) -> Unit) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoTitle: TextView = itemView.findViewById(R.id.name1)
        val videoImageView: ImageView = itemView.findViewById(R.id.profile1)    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoItem = videoList[position]
        holder.videoTitle.text = videoItem.title

        Glide.with(holder.itemView.context)
            .load(videoItem.path)
            .into(holder.videoImageView)
        holder.itemView.setOnClickListener { onClick(videoItem) }
    }

    override fun getItemCount(): Int = videoList.size
}