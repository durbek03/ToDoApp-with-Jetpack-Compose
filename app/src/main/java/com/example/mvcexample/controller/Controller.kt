package com.example.mvcexample.controller

import android.content.Context
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task

class Controller(val appDatabase: AppDatabase) {

    suspend fun getAllTasks(): List<Task> {
        return appDatabase.taskDao().getAllTask()
    }

    suspend fun getCategoryWithTask(): List<CategoryWithTask> {
        return appDatabase.categoryDao().getAllDataFromCategory()
    }
}