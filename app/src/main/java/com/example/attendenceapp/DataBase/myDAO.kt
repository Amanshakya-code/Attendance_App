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

    @Query("SElECT * FROM statustb WHERE SID LIKE :sid AND date LIKE :date")
    fun getStatus(sid:Int,date:String):LiveData<statusEntity>

    @Query("SELECT date FROM statustb WHERE CID==:cid GROUP BY substr(date,3,8)")
    fun getDistinctMonth(cid:Int):LiveData<List<String>>

    @Query("SElECT * FROM statustb WHERE SID LIKE :sid AND date LIKE :date")
    fun getstatusforSheet(sid:Int,date:String):LiveData<statusEntity>

}