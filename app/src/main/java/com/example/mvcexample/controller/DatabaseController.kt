package com.example.mvcexample.controller

import android.content.Context
import android.util.Log
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
            Log.d(TAG, "getCategoryWithTask: new value")
            viewModel.setCategoryList(it)
        }
        return list
    }

    suspend fun updateCategory(category: Category, oldName: String): Boolean {
        return if (category.categoryName == oldName) {
            appDatabase.categoryDao().updateCategory(category = category)
            true
        } else {
            val checkIfCategoryNameExists = appDatabase.categoryDao()
                .checkIfCategoryNameExists(categoryName = category.categoryName!!)
            if (checkIfCategoryNameExists != null) {
                false
            } else {
                appDatabase.categoryDao().updateCategory(category = category)
                true
            }
        }
    }

    suspend fun addDefaultLists() {
        val checkIfEmpty = appDatabase.categoryDao().checkIfEmpty()
        if (checkIfEmpty == null) {
            val categories = listOf(
                Category("Inbox", R.color.grey, R.color.dark_black),
                Category("Work", R.color.green, R.color.white),
                Category("Shopping", R.color.red, R.color.white),
                Category("Family", R.color.yellow, R.color.dark_black),
                Category("Personal", R.color.purple, R.color.white)
            )
            categories.forEach { list ->
                appDatabase.categoryDao().addCategory(list)
            }
        }
    }
}