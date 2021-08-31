package com.example.attendenceapp.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.attendenceapp.Model.ClassItem
import com.example.attendenceapp.Model.StudentEntity
import com.example.attendenceapp.Model.statusEntity

@Database(entities = [ClassItem::class,StudentEntity::class,statusEntity::class],version = 3,exportSchema = false)
abstract class DatabaseInstance : RoomDatabase(){
    abstract fun getmyDao() : myDAO
    companion object{
        @Volatile
        private var INSTANCE:DatabaseInstance?=null
        fun getDatabaseInstance(context:Context):DatabaseInstance{
            if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,DatabaseInstance::class.java,"attendenceDB").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }
}