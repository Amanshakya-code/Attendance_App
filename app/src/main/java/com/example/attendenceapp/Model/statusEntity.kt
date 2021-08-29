package com.example.attendenceapp.Model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "statustb",indices = [Index(value = ["SID"], unique = true)])
data class statusEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    val SID:Int,
    val date:String,
    val status:String
        )