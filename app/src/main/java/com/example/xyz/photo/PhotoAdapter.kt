package com.example.xyz.photo

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.bumptech.glide.Glide
    import com.example.xyz.R

class  PhotoAdapter(private val photoList: List<PhotoFragment.PhotoItem>, private val onClick: (PhotoFragment.PhotoItem) -> Unit) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

        class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val photoName: TextView = itemView.findViewById(R.id.name2)
            val photoImageView: ImageView = itemView.findViewById(R.id.profile2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_photo, parent, false)
            return PhotoViewHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
            val photoItem = photoList[position]
            holder.photoName.text = photoItem.name

            Glide.with(holder.itemView.context)
                .load(photoItem.path)
                .into(holder.photoImageView)
            holder.itemView.setOnClickListener { onClick(photoItem) }
        }

        override fun getItemCount(): Int = photoList.size
    }
