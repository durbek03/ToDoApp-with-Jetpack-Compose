package com.example.mvcexample.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import com.example.mvcexample.R
import com.example.mvcexample.room.entity.Category

class AddTaskViewModel: ViewModel() {
    var taskTextFieldState by mutableStateOf("")
    fun setTextFieldState(str: String) {
        taskTextFieldState = str
    }

    var date by mutableStateOf("")
    fun setdate(date: String) {
        this.date = date
    }

    var time by mutableStateOf("")
    fun settime(time: String) {
        this.time = time
    }

    var category: Category? by mutableStateOf(null)
    fun setcategory(category: Category?) {
        this.category = category
    }

    val pickedCategoryColor: MutableState<Int>
        get() {
        if (category == null) {
            return mutableStateOf(R.color.white)
        } else {
            return mutableStateOf(category!!.backgroundColor!!)
        }
    }
}