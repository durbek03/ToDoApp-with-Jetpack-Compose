package com.example.mvcexample

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import com.example.mvcexample.globalui.FabState
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    var categoryList: MutableLiveData<List<CategoryWithTask>> = MutableLiveData()
    fun setCategoryList(list: List<CategoryWithTask>) {
        categoryList.value = list
    }

    var taskList by mutableStateOf(listOf<Task>())

    fun setTasklist(list: List<Task>) {
        taskList = list
    }
}