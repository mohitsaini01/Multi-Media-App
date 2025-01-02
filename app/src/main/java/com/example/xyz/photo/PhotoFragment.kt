package com.example.xyz.photo

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.xyz.R

class PhotoFragment : Fragment() {

    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private val photoList = mutableListOf<PhotoItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)

        photoRecyclerView = view.findViewById(R.id.rv_photo)

        loadPhotos()

        photoAdapter = PhotoAdapter(photoList) { photoItem ->
            openPhotoViewer(photoItem)
        }

       photoRecyclerView.layoutManager = GridLayoutManager(context,2)
       // photoAdapter = PhotoAdapter(photoList)
        photoRecyclerView.adapter = photoAdapter

        return view
    }
    private fun loadPhotos() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
        )

        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val name = it.getString(nameColumn)
                val path = it.getString(dataColumn)
                photoList.add(PhotoItem(name, path))
            }
        }
        photoList.reverse()
        photoRecyclerView.adapter?.notifyDataSetChanged()
    }
    data class PhotoItem(val name: String, val path: String)

    private fun openPhotoViewer(photoItem: PhotoItem) {
        val intent = Intent(context, PhotoViewerActivity::class.java)
        val photoPaths = photoList.map { it.path } // List of all photo paths
        val currentPosition = photoList.indexOf(photoItem) // Position of the clicked photo

        intent.putStringArrayListExtra("photo_paths", ArrayList(photoPaths))
        intent.putExtra("photo_position", currentPosition)

        startActivity(intent)
    }



}
