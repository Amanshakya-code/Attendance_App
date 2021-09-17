package com.example.attendenceapp.OnBoardingScreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.attendenceapp.MainActivity
import com.example.attendenceapp.OnboardingActivity
import com.example.attendenceapp.R
import kotlinx.android.synthetic.main.fragment_third.*


class ThirdFragment : Fragment(R.layout.fragment_third) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterintomainscreenbtn.setOnClickListener {
            startActivity(Intent(requireContext(),MainActivity::class.java))
            (activity as OnboardingActivity).window.statusBarColor = resources.getColor(R.color.light_blue,(activity as OnboardingActivity).theme)
        }
    }
}