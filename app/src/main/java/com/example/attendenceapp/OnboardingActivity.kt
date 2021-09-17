package com.example.attendenceapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.attendenceapp.Constant.constant.Companion.PREFKEY
import com.example.attendenceapp.OnBoardingScreen.ViewPager
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        this.window.statusBarColor = resources.getColor(R.color.onboardingcolor,this.theme)
        val sp = getSharedPreferences("mypref",Context.MODE_PRIVATE)
        var check = sp.getBoolean(PREFKEY,false)
        if(check == true)
        {
            startActivity(Intent(this,MainActivity::class.java))
        }
        else{
            viewPager = ViewPager(supportFragmentManager,1)
            pager.adapter = viewPager
        }

    }
}