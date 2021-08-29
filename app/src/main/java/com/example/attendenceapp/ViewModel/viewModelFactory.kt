package com.example.attendenceapp.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.attendenceapp.Repository.repository

class viewModelFactory(val app:Application,private val respository:repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return attendenceViewmodel(app,respository) as T
    }

}