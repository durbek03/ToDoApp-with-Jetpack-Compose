package com.example.mvcexample.controller

import android.content.Context
import com.example.mvcexample.R
import com.example.mvcexample.viewmodels.MainViewModel
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.util.*

class DatabaseController(context: Context, val viewModel: MainViewModel) {

    val appDatabase = AppDatabase.getInstance(context)
    private val TAG = "Controller"
    suspend fun addTask(task: Task): Long {
        return appDatabase.taskDao().addTask(task)
    }

    suspend fun updateTask(task: Task) {
        appDatabase.taskDao().updateTask(task)
    }

    suspend fun updateTaskColors(color: Int) {
        appDatabase.taskDao()
    }

    suspend fun getTodaysTask(): Flow<List<Task>> {
        val calendar = Calendar.getInstance()
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR)
        val today = "$day.$month.$year"
        val todayTasks = appDatabase.taskDao().getTodayTasks(today)
        todayTasks.collect {
            viewModel.setTasklist(it)
        }
        return todayTasks
    }

    suspend fun addCategory(category: Category): Boolean {
        val check =
            appDatabase.categoryDao().checkIfCategoryNameExists(category.categoryName!!)

        return if (check == null) {
            appDatabase.categoryDao().addCategory(category)
            true
        } else false
    }

    suspend fun getCategoryWithTask(): Flow<List<CategoryWithTask>> {
        val list = appDatabase.categoryDao().getAllDataFromCategory()
        list.collect {
            viewModel.setCategoryList(it)
        }
        return list
    }
}