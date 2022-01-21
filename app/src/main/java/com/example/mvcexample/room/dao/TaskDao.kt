package com.example.mvcexample.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvcexample.room.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun addTask(task: Task): Long

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("select * from task where task.date is '' or date = :date")
    fun getTodayTasks(date: String): Flow<List<Task>>
}