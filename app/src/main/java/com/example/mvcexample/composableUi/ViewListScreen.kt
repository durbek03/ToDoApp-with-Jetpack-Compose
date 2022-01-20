package com.example.mvcexample.composableUi

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.viewmodels.MainViewModel
import com.example.mvcexample.R
import com.example.mvcexample.globalui.FabState
import com.example.mvcexample.viewmodels.AddTaskViewModel
import com.example.mvcexample.viewmodels.FabControlViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class ViewListScreen(val viewModel: MainViewModel, val fabViewModel: FabControlViewModel) {
    private val TAG = "ViewListScreen"

    @Composable
    fun ViewListScreen(id: Int) {
        val uiController = rememberSystemUiController()
        fabViewModel.setFabvisibility(true)
        uiController.setStatusBarColor(Color.Black, darkIcons = false)

        val categoryList = viewModel.categoryList.observeAsState().value
        val categoryWithTasks by produceState(
            initialValue = CategoryWithTask(
                Category(),
                listOf()
            )
        ) {
            var cat: CategoryWithTask?
            repeat(categoryList!!.size) {
                if (categoryList[it].category.id == id) {
                    cat = categoryList[it]
                    value = cat!!
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(10.dp)
                    )
                    .background(Color.Gray)
            )
        }

        MainListScreen(categoryWithTasks = categoryWithTasks)
    }

    @Composable
    fun MainListScreen(categoryWithTasks: CategoryWithTask) {
        val interactionSource = remember {
            MutableInteractionSource()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
                .clickable(indication = null, interactionSource = interactionSource) {
                    if (fabViewModel.showFabDialog) {
                        fabViewModel.setFabstate(FabState.NOTROTATED)
                    }
                    if (fabViewModel.showListDialog) {
                        fabViewModel.setShowListdialog(false)
                    }
                },
            shape = RoundedCornerShape(10.dp),
            color = colorResource(
                id = if (categoryWithTasks.category.backgroundColor != null) categoryWithTasks.category.backgroundColor!! else R.color.white
            )
        ) {
        }
    }
}