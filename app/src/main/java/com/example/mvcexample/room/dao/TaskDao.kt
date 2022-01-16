package com.example.mvcexample.room.dao

import androidx.room.*
import com.example.mvcexample.room.entity.Task

@Dao
interface TaskDao {
    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("select * from task order by title")
    suspend fun getAllTask(): List<Task>
}