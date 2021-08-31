package com.example.attendenceapp.DataBase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.statusEntity

@Dao
interface myDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertClassItem(classItem: ClassItem)

    @Query("SELECT * FROM class")
    fun getAllSubject():LiveData<List<ClassItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudentItem(studentEntity: StudentEntity)

    @Query("SELECT * FROM student WHERE C_id==:cid")
    fun getAllStudent(cid:Int):LiveData<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStatus(statusEntity: statusEntity)

    @Query("SElECT status FROM statustb WHERE SID==:sid AND date ==:date")
    suspend fun getStatus(sid:Int,date:String):String

    @Query("SELECT date FROM statustb WHERE CID==:cid GROUP BY substr(date,3,8)")
    fun getDistinctMonth(cid:Int):LiveData<List<String>>

}