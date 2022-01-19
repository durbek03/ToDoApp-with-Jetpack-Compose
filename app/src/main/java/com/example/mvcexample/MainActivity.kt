package com.example.mvcexample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvcexample.composableUi.AddTaskScreen
import com.example.mvcexample.controller.Controller
import com.example.mvcexample.globalui.CustomFloatingActionButton
import com.example.mvcexample.globalui.FabState
import com.example.mvcexample.globalui.ListDialog
import com.example.mvcexample.composableUi.HomeScreen
import com.example.mvcexample.navigation.Destinations
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.Task
import com.example.mvcexample.ui.theme.MVCexampleTheme
import com.example.mvcexample.utils.DialogItemPicker
import com.example.mvcexample.viewmodels.AddTaskViewModel
import com.example.mvcexample.viewmodels.FabControlViewModel
import com.example.mvcexample.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class MainActivity : ComponentActivity() {
    lateinit var controller: Controller
    lateinit var viewModel: MainViewModel
    lateinit var fabViewModel: FabControlViewModel
    lateinit var addTaskViewModel: AddTaskViewModel
    val TAG = "MAINACTIVITY"

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        fabViewModel = ViewModelProvider(this)[FabControlViewModel::class.java]
        addTaskViewModel = ViewModelProvider(this)[AddTaskViewModel::class.java]
        setContent {
            MVCexampleTheme {
                Surface(color = MaterialTheme.colors.background) {

                    controller =
                        Controller(AppDatabase.getInstance(LocalContext.current), viewModel)
                    LaunchedEffect(true) {
                        controller.getCategoryWithTask()
                    }
                    LaunchedEffect(key1 = true) {
                        controller.getTodaysTask()
                    }

                    val scope = rememberCoroutineScope()
                    val interactionSource = remember {
                        MutableInteractionSource()
                    }
                    val categoryList = viewModel.categoryList.observeAsState().value
                    val taskList = viewModel.taskList.observeAsState().value

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.HomeScreen.route
                    ) {
                        composable(Destinations.HomeScreen.route) {
                            fabViewModel.setFabvisibility(true)
                            val homeScreen = HomeScreen(categoryList, taskList)
                            homeScreen.HomeScreen(modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource
                                ) {
                                    if (fabViewModel.fabState == FabState.ROTATED) fabViewModel.setFabstate(
                                        FabState.NOTROTATED
                                    )
                                    if (fabViewModel.showListDialog) fabViewModel.setShowListdialog(
                                        false
                                    )
                                }
                            )
                        }
                        composable(Destinations.AddTaskScreen.route) {
                            val context = LocalContext.current
                            fabViewModel.setFabvisibility(false)
                            val taskScreen = AddTaskScreen(addTaskViewModel, navController, mainViewModel = viewModel) {
                                if (addTaskViewModel.taskTextFieldState.isEmpty()) {
                                    Toast.makeText(context, "Enter task description", Toast.LENGTH_SHORT).show()
                                    return@AddTaskScreen
                                }
                                if (addTaskViewModel.date.isEmpty()) {
                                    Toast.makeText(context, "Pick date", Toast.LENGTH_SHORT).show()
                                    return@AddTaskScreen
                                }
                                if (addTaskViewModel.time.isEmpty()) {
                                    Toast.makeText(context, "Pick time", Toast.LENGTH_SHORT).show()
                                    return@AddTaskScreen
                                }
                                if (addTaskViewModel.category == null) {
                                    Toast.makeText(
                                        context,
                                        "Pick list which task should belong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@AddTaskScreen
                                }
                                val task = Task(addTaskViewModel.category!!.id, addTaskViewModel.taskTextFieldState, addTaskViewModel.date, addTaskViewModel.time, addTaskViewModel.category!!.backgroundColor)
                                scope.launch(Dispatchers.IO) {
                                    controller.addTask(task = task)
                                }
                                navController.popBackStack()
                            }
                            taskScreen.AddTaskScreen()
                        }
                    }

                    AnimatedVisibility(visible = fabViewModel.fabVisibility) {
                        CustomFloatingActionButton(
                            viewModel = fabViewModel,
                            object : DialogItemPicker {
                                override fun taskPicked() {
                                    fabViewModel.setFabstate(FabState.NOTROTATED)
                                    if (navController.currentDestination?.route != Destinations.AddTaskScreen.route) {
                                        navController.navigate(Destinations.AddTaskScreen.route)
                                    }
                                }

                                override fun listPicked() {
                                    fabViewModel.setShowListdialog(true)
                                    fabViewModel.setFabstate(FabState.NOTROTATED)
                                }
                            })
                    }

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
                                    val addCategory = controller.addCategory(category)
                                    if (!addCategory) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "List with this name already exists",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (fabViewModel.showListDialog) {
            fabViewModel.setShowListdialog(false)
        } else {
            super.onBackPressed()
        }
    }
}