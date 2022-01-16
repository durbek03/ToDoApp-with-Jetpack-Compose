package com.example.mvcexample.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

@Entity
class CategoryWithTask(
    @Embedded
    var category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val taskList: List<Task>
)