package com.example.attendenceapp.OnBoardingScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPager(fragmentManager: FragmentManager,behavior:Int) : FragmentPagerAdapter(fragmentManager,behavior) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return FirstFragment()
            }
            1->{
                return SecondFragment()
            }
            2->{
                return ThirdFragment()
            }
        }
        return FirstFragment()
    }
}