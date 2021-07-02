package com.example.viewpager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.viewpager.Fragment1
import com.example.viewpager.Fragment2
import com.example.viewpager.Fragment3

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle : Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {Fragment1()}
            1 -> {Fragment2()}
            2 -> {Fragment3()}
            else -> {Fragment1()}
        }
    }
}