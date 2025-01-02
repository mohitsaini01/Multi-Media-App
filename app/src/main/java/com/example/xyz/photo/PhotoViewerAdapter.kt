package com.example.xyz.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xyz.R

class PhotoViewerAdapter(private val photoList: List<String>) : RecyclerView.Adapter<PhotoViewerAdapter.PhotoViewerViewHolder>() {

    class PhotoViewerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewerViewHolder, position: Int) {
        val photoPath = photoList[position]
        Glide.with(holder.itemView.context)
            .load(photoPath)
            .into(holder.photoImageView)
    }

    override fun getItemCount(): Int = photoList.size
}
