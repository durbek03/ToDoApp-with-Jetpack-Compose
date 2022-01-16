package com.example.mvcexample.room.dao

import androidx.room.*
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask

@Dao
interface CategoryDao {
    @Insert
    fun addCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("select * from Category")
    suspend fun getAllDataFromCategory(): List<CategoryWithTask>
}