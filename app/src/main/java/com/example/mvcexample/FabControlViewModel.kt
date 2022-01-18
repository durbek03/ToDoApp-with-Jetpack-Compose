package com.example.mvcexample

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.mvcexample.globalui.FabState

class FabControlViewModel: ViewModel() {
    //fab General
    var fabState by mutableStateOf(FabState.NOTROTATED)

    var showFabDialog by mutableStateOf(false)

    fun setFabstate(state: FabState) {
        fabState = state
    }

    fun setShowFabdialog(show: Boolean) {
        showFabDialog = show
    }

    //fabAnimation
    var backColorState by mutableStateOf(value = Color.White)
    fun setFabBackColor(color: Color) {
        backColorState = color
    }

    var iconColor by mutableStateOf(value = Color(0xFF006CFF))
    fun setFabIconColor(color: Color) {
        iconColor = color
    }

    var rotationState by mutableStateOf(0f)
    fun setRotationstate(degree: Float) {
        rotationState = degree
    }

    //List Dialog states
    var selectedColorState by mutableStateOf(R.color.blue)
    fun setSelectedColorstate(color: Int) {
        selectedColorState = color
    }

    var showListDialog by mutableStateOf(value = false)
    fun setShowListdialog(show: Boolean) {
        showListDialog = show
    }

    var listDialogTextFieldState by mutableStateOf("")
    fun setlistDialogTFState(state: String) {
        listDialogTextFieldState = state
    }
}
