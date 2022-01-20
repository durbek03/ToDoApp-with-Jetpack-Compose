package com.example.mvcexample.room.entity

import androidx.compose.runtime.MutableState
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
    @ColumnInfo
    var textColor: Int? = null

    constructor()
    constructor(categoryName: String?, backgroundColor: Int?, textColor: Int?) {
        this.categoryName = categoryName
        this.backgroundColor = backgroundColor
        this.textColor = textColor
    }
}