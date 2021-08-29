package com.example.attendenceapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class")
data class ClassItem(
    @PrimaryKey(autoGenerate = true)
    val C_id:Int? = null,

    val ClassName:String,
    val SubjectName:String
)