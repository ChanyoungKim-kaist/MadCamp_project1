package com.example.viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabIcon = listOf(
            R.drawable.ic_baseline_people_24,
            R.drawable.ic_baseline_photo_library_24,
            R.drawable.ic_baseline_music_note_24,
        )

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager_2)
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) {tab, position->
            when(position) {
                0->{
                    tab.text="Contact"
                    tab.setIcon(tabIcon[position])
                }
                1->{
                    tab.text="Gallery"
                    tab.setIcon(tabIcon[position])
                }
                2->{
                    tab.text="Free"
                    tab.setIcon(tabIcon[position])
                }
            }
        }.attach()
    }
}