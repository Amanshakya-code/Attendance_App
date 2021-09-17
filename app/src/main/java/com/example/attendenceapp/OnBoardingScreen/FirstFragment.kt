package com.example.attendenceapp.OnBoardingScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.attendenceapp.OnboardingActivity
import com.example.attendenceapp.R

class FirstFragment : Fragment(R.layout.fragment_first) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as OnboardingActivity).window.statusBarColor = resources.getColor(R.color.darkYellow,(activity as OnboardingActivity).theme)
    }
}