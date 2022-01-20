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
    var color: Int? = null
    @ColumnInfo
    var isDone: Boolean? = null
    @ColumnInfo
    var textColor: Int? = null

    constructor()
    constructor(
        categoryId: Int?,
        title: String?,
        date: String?,
        time: String?,
        color: Int?,
        isDone: Boolean?,
        textColor: Int?
    ) {
        this.categoryId = categoryId
        this.title = title
        this.date = date
        this.time = time
        this.color = color
        this.isDone = isDone
        this.textColor = textColor
    }

}