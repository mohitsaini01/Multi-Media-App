package com.example.xyz.video

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
import com.example.xyz.R


class VideoFragment : Fragment() {

    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private val videoList =  mutableListOf<VideoItem>()// Add your video titles here

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        videoRecyclerView = view.findViewById(R.id.rv1)
        videoRecyclerView.layoutManager = GridLayoutManager(context,2)

        loadVideos()

        videoAdapter = VideoAdapter(videoList) { videoItem ->
            openVideoPlayer(videoItem)
        }

       // videoAdapter = VideoAdapter(videoList)
        videoRecyclerView.adapter = videoAdapter

        return view
    }
    private fun loadVideos() {
        val contentResolver: ContentResolver = requireContext().contentResolver
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA
        )

        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (it.moveToNext()) {
                val title = it.getString(titleColumn)
                val path = it.getString(dataColumn)
                videoList.add(VideoItem(title, path))
            }
        }

        videoList.reverse()
        videoRecyclerView.adapter?.notifyDataSetChanged()
    }
    data class VideoItem(val title : String, val path: String)

//    private fun openVideoPlayer(videoItem: VideoItem) {
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(Uri.parse(videoItem.path), "video/*")
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        }
//        startActivity(intent)
//    }

    

    private fun openVideoPlayer(videoItem: VideoItem) {
        val intent = Intent(context, VideoPlayerActivity::class.java)
        intent.putExtra("video_path", videoItem.path)
        startActivity(intent)
    }

}