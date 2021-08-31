package com.example.attendenceapp.Repository

import com.example.attendenceapp.DataBase.DatabaseInstance
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.statusEntity

class repository(val db:DatabaseInstance) {

    suspend fun upsertclassItem(classItem: ClassItem){
        db.getmyDao().upsertClassItem(classItem)
    }

    fun getAllClassdata() = db.getmyDao().getAllSubject()


    suspend fun upsertStudentItem(studentEntity: StudentEntity){
        db.getmyDao().upsertStudentItem(studentEntity)
    }

    fun getAllStudentData(cid:Int) = db.getmyDao().getAllStudent(cid)

    suspend fun upsertStatus(statusEntity: statusEntity){
        db.getmyDao().upsertStatus(statusEntity)
    }

    suspend fun getStatus(sid:Int,date:String) =  db.getmyDao().getStatus(sid,date)
}