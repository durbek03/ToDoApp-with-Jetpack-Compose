package com.example.mvcexample

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mvcexample.controller.Controller
import com.example.mvcexample.globalui.CustomFloatingActionButton
import com.example.mvcexample.globalui.FabState
import com.example.mvcexample.globalui.ListDialog
import com.example.mvcexample.home.HomeScreen
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.ui.theme.MVCexampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    lateinit var controller: Controller
    lateinit var viewModel: MainViewModel
    lateinit var fabViewModel: FabControlViewModel
    val TAG = "MAINACTIVITY"

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        fabViewModel = ViewModelProvider(this)[FabControlViewModel::class.java]
        setContent {
            MVCexampleTheme {
                Surface(color = MaterialTheme.colors.background) {

                    controller =
                        Controller(AppDatabase.getInstance(LocalContext.current), viewModel)
                    val scope = rememberCoroutineScope()
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }

                    LaunchedEffect(true) {
                        controller.getCategoryWithTask()
                    }
                    val categoryList = viewModel.categoryList.observeAsState().value
                    Log.d(TAG, "onCreate: created")

                    val homeScreen = HomeScreen(viewModel, categoryList)
                    homeScreen.HomeScreen(modifier = Modifier
                        .clickable(indication = null, interactionSource = interactionSource) {
                            if (fabViewModel.fabState == FabState.ROTATED) fabViewModel.setFabstate(
                                FabState.NOTROTATED
                            )
                            if (fabViewModel.showListDialog) fabViewModel.setShowListdialog(
                                false
                            )
                        }
                    )
                    CustomFloatingActionButton(viewModel = fabViewModel)
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimatedVisibility(visible = fabViewModel.showListDialog) {
                            ListDialog(viewModel = fabViewModel, addClicked = {
                                if (fabViewModel.listDialogTextFieldState.isEmpty()) {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Enter name for List",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@ListDialog
                                }
                                fabViewModel.setShowListdialog(false)
                                val category = Category(
                                    fabViewModel.listDialogTextFieldState,
                                    fabViewModel.selectedColorState
                                )
                                scope.launch {
                                    controller.addCategory(category)
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}