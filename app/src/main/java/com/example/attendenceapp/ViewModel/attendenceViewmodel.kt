package com.example.attendenceapp.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.statusEntity
import com.example.attendenceapp.Repository.repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class attendenceViewmodel(app:Application,private val repository: repository): AndroidViewModel(app) {

    var statusView:MutableLiveData<HashMap<Int,String>> = MutableLiveData()
    var sheetStatus:MutableLiveData<HashMap<String,String>> = MutableLiveData()
    var sheetmap:HashMap<String,String> = HashMap()
    var map:HashMap<Int,String> = HashMap()
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

    fun getStatus(sid:Int,date:String) = viewModelScope.launch {
        val status = repository.getStatus(sid,date)
        Log.i("anany","$sid --- > $status")
        map.put(sid,status)
        statusView.postValue(map)
    }


    fun getStatusForSheet(sid: Int,date: String) = viewModelScope.launch {
        val status = repository.getStatus(sid,date)
        sheetmap.put(date,status)
        Log.i("sisi","$sheetmap")
        sheetStatus.postValue(sheetmap)

    }

    fun getALlDistinctMonthData(cid:Int) = repository.getAllDistinctMonths(cid)

    fun getAllstatusfirsheet(sid:Int,date:String) = repository.getAllsheetstatus(sid,date)
}