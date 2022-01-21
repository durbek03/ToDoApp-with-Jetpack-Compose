package com.example.mvcexample.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
class Task {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo
    var categoryId: Int? = null
    @ColumnInfo
    var title: String? = null
    @ColumnInfo
    var date: String? = null
    @ColumnInfo
    var time: String? = null
    @ColumnInfo
    var isDone: Boolean? = null

    constructor()
    constructor(
        categoryId: Int?,
        title: String?,
        date: String?,
        time: String?,
        isDone: Boolean?,
    ) {
        this.categoryId = categoryId
        this.title = title
        this.date = date
        this.time = time
        this.isDone = isDone
    }
}