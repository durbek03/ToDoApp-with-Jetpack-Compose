package com.example.mvcexample.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.mvcexample.R
import com.example.mvcexample.room.entity.Category

class ViewListViewModel : ViewModel() {
    var category by mutableStateOf(Category())
    fun setcategory(category: Category) {
        this.category = category
    }

    var updateDialogVisibility by mutableStateOf(false)
    fun setDialogVisibilityState(visible: Boolean) {
        updateDialogVisibility = visible
    }

    var updateTFState by mutableStateOf("")
    fun setTfState(str: String) {
        updateTFState = str
    }

    var selectedColorState by mutableStateOf(R.color.black)
    fun setSelectedColor(color: Int) {
        selectedColorState = color
    }
}
