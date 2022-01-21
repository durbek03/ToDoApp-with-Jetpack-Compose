package com.example.mvcexample.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvcexample.room.dao.CategoryDao
import com.example.mvcexample.room.dao.TaskDao
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.Task

@Database(entities = [Category::class, Task::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "mvcdatabase")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return appDatabase!!
        }
    }
}