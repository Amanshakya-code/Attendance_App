package com.example.attendenceapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student")
data class StudentEntity (
    @PrimaryKey(autoGenerate = true)
    val Sid:Int?= null,

    val C_id:Int,
    val st_roll:String,
    val stname:String,
    var status:String = ""
        )