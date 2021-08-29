package com.example.attendenceapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.statusEntity
import com.example.attendenceapp.Repository.repository
import kotlinx.coroutines.launch

class attendenceViewmodel(app:Application,private val repository: repository): AndroidViewModel(app) {

    fun saveClassData(classItem: ClassItem)
    {
        viewModelScope.launch {
            repository.upsertclassItem(classItem)
        }
    }
    fun getClassData() = repository.getAllClassdata()

    fun saveStudentData(studentEntity: StudentEntity){
        viewModelScope.launch {
            repository.upsertStudentItem(studentEntity)
        }
    }
    fun getAllStudent(cid:Int) = repository.getAllStudentData(cid)

    fun saveStatus(statusEntity: statusEntity){
        viewModelScope.launch {
            repository.upsertStatus(statusEntity)
        }
    }

    fun getStatus(sid:Int,date:String) = repository.getStatus(sid,date)
}