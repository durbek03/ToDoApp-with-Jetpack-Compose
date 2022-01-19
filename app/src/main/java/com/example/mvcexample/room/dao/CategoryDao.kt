package com.example.mvcexample.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun addCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("select * from Category")
    fun getAllDataFromCategory(): Flow<List<CategoryWithTask>>

    @Query("select * from category where categoryName = :categoryName")
    suspend fun checkIfCategoryNameExists(categoryName: String): Category?
}