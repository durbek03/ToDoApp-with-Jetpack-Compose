package com.example.mvcexample.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo
    var categoryName: String? = null
    @ColumnInfo
    var backgroundColor: Int? = null

    constructor()
    constructor(categoryName: String?, backgroundColor: Int?) {
        this.categoryName = categoryName
        this.backgroundColor = backgroundColor
    }
}