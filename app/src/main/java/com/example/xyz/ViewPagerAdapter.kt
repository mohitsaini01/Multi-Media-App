package com.example.xyz

import com.example.xyz.video.VideoFragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.xyz.music.MusicFragment
import com.example.xyz.photo.PhotoFragment

class ViewPagerAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            // Return the number of tabs (Fragments)
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            // Return the appropriate fragment based on the position
            return when (position) {
                0 -> MusicFragment()
                1 -> VideoFragment()
                2 -> PhotoFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

