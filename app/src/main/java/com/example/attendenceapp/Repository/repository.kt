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

    suspend fun updateClassdetails(classItem: ClassItem){
        db.getmyDao().updateClass(classItem)
    }

    fun getStatus(sid:Int,date:String) =  db.getmyDao().getStatus(sid,date)

    fun getAllDistinctMonths(cid:Int) = db.getmyDao().getDistinctMonth(cid)

    fun getAllsheetstatus(sid:Int,date:String) = db.getmyDao().getstatusforSheet(sid,date)

    suspend fun deleteClass(cid:Int){
        db.getmyDao().deleteClass(cid)
    }
    suspend fun deleteStudentFromClass(cid:Int){
        db.getmyDao().deleteAllStudentFromClass(cid)
    }
    suspend fun deleteStatusFromClass(cid:Int){
        db.getmyDao().deleteAllstatusFromClass(cid)
    }

    suspend fun updateStudent(studentEntity:StudentEntity){
        db.getmyDao().updateStudent(studentEntity)
    }
    suspend fun deleteStudent(sid:Int){
        db.getmyDao().deleteStudent(sid)
    }
    suspend fun deleteStatusForStudent(sid:Int){
        db.getmyDao().deleteStudentStatus(sid)
    }

    fun getClassStatus(cid:Int,date:String,status:String) = db.getmyDao().getAllStatusOfStudent(cid,date,status)



}