package com.example.mvcexample.controller

import androidx.lifecycle.LiveData
import com.example.mvcexample.MainViewModel
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class Controller(val appDatabase: AppDatabase, val viewModel: MainViewModel) {

    suspend fun addCategory(category: Category) {
        appDatabase.categoryDao().addCategory(category)
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return appDatabase.taskDao().getAllTask()
    }

    suspend fun getCategoryWithTask(): Flow<List<CategoryWithTask>> {
        val list = appDatabase.categoryDao().getAllDataFromCategory()
        list.collect {
            viewModel.setCategoryList(it)
        }
        return list
    }
}