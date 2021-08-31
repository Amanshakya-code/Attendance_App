package com.example.attendenceapp.Model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "statustb",indices = [Index(value = ["SID","date"], unique = true)])
data class statusEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,

    val CID:Int,
    val SID:Int,
    val date:String,
    val status:String
        )