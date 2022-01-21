package com.example.mvcexample

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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mvcexample.composableUi.AddTaskScreen
import com.example.mvcexample.controller.DatabaseController
import com.example.mvcexample.globalui.CustomFloatingActionButton
import com.example.mvcexample.globalui.FabState
import com.example.mvcexample.globalui.ListDialog
import com.example.mvcexample.composableUi.HomeScreen
import com.example.mvcexample.composableUi.ListTaskSelection
import com.example.mvcexample.composableUi.ViewListScreen
import com.example.mvcexample.controller.AlarmController
import com.example.mvcexample.globalui.UpdateListDialog
import com.example.mvcexample.navigation.Destinations
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task
import com.example.mvcexample.ui.theme.MVCexampleTheme
import com.example.mvcexample.utils.DialogItemPicker
import com.example.mvcexample.viewmodels.AddTaskViewModel
import com.example.mvcexample.viewmodels.FabControlViewModel
import com.example.mvcexample.viewmodels.MainViewModel
import com.example.mvcexample.viewmodels.ViewListViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : ComponentActivity() {
    lateinit var databaseController: DatabaseController
    lateinit var alarmController: AlarmController
    lateinit var viewModel: MainViewModel
    lateinit var fabViewModel: FabControlViewModel
    lateinit var addTaskViewModel: AddTaskViewModel
    lateinit var viewListViewModel: ViewListViewModel
    val TAG = "MAINACTIVITY"

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        fabViewModel = ViewModelProvider(this)[FabControlViewModel::class.java]
        addTaskViewModel = ViewModelProvider(this)[AddTaskViewModel::class.java]
        viewListViewModel = ViewModelProvider(this)[ViewListViewModel::class.java]
        setContent {
            MVCexampleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val context = LocalContext.current
                    databaseController =
                        DatabaseController(LocalContext.current, viewModel)
                    alarmController = AlarmController(LocalContext.current)

                    LaunchedEffect(viewListViewModel.updateDialogVisibility) {
                        databaseController.getCategoryWithTask()
                    }
                    LaunchedEffect(true) {
                        databaseController.getTodaysTask()
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
                            val homeScreen =
                                HomeScreen(categoryList, taskList, object : ListTaskSelection {
                                    override fun onTaskSelectedChanged(task: Task) {
                                        task.isDone = !task.isDone!!
                                        if (task.isDone!! && task.time!!.isNotEmpty()) {
                                            alarmController.setAlarm(
                                                task.id!!,
                                                task.date!!,
                                                task.time!!,
                                                task.title!!
                                            )
                                        } else if (!task.isDone!! && task.time!!.isNotEmpty()) {
                                            alarmController.disableAlarm(task.id!!)
                                        }
                                        scope.launch {
                                            databaseController.updateTask(task)
                                        }
                                    }

                                    override fun onListSelected(category: CategoryWithTask) {
                                        val id = category.category.id
                                        navController.navigate(Destinations.ViewListScreen.route + "/$id")
                                    }
                                })
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
                            fabViewModel.setFabvisibility(false)
                            val taskScreen = AddTaskScreen(
                                addTaskViewModel,
                                navController,
                                mainViewModel = viewModel
                            ) {
                                if (addTaskViewModel.taskTextFieldState.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        "Enter task description",
                                        Toast.LENGTH_SHORT
                                    ).show()
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
                                val task = Task(
                                    addTaskViewModel.category!!.id,
                                    addTaskViewModel.taskTextFieldState,
                                    addTaskViewModel.date,
                                    addTaskViewModel.time,
                                    false
                                )
                                scope.launch(Dispatchers.IO) {
                                    val id = databaseController.addTask(task = task)
                                    withContext(Dispatchers.Main) {
                                        navController.popBackStack()
                                        if (task.time!!.isNotEmpty()) {
                                            alarmController.setAlarm(
                                                id.toInt(),
                                                task.date!!,
                                                task.time!!,
                                                task.title!!
                                            )
                                        }
                                    }
                                }
                            }
                            taskScreen.AddTaskScreen()
                        }
                        composable(
                            Destinations.ViewListScreen.route + "/{id}",
                            arguments = listOf(navArgument("id") {
                                type = NavType.IntType
                            })
                        ) { entry ->
                            val viewListScreen = ViewListScreen(
                                viewListViewModel,
                                viewModel,
                                fabViewModel,
                                object : ViewListScreen.OnTaskSelectChangedListener {
                                    override fun onTaskSelectChanged(task: Task) {
                                        task.isDone = !task.isDone!!
                                        if (task.isDone!! && task.time!!.isNotEmpty()) {
                                            alarmController.setAlarm(
                                                task.id!!,
                                                task.date!!,
                                                task.time!!,
                                                task.title!!
                                            )
                                        } else if (!task.isDone!! && task.time!!.isNotEmpty()) {
                                            alarmController.disableAlarm(task.id!!)
                                        }
                                        scope.launch {
                                            databaseController.updateTask(task)
                                        }
                                    }
                                }, databaseController)
                            viewListScreen.ViewListScreen(entry.arguments!!.getInt("id"))
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
                                    viewListViewModel.setDialogVisibilityState(false)
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
                                var textColor: Int
                                val backColor = fabViewModel.selectedColorState
                                textColor = if (backColor in listOf<Int>(
                                        R.color.grey,
                                        R.color.yellow
                                    )
                                ) R.color.dark_black else R.color.white
                                val category = Category(
                                    fabViewModel.listDialogTextFieldState,
                                    fabViewModel.selectedColorState,
                                    textColor
                                )
                                scope.launch {
                                    val addCategory = databaseController.addCategory(category)
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
                        AnimatedVisibility(visible = viewListViewModel.updateDialogVisibility) {
                            UpdateListDialog(
                                category = viewListViewModel.category,
                                viewModel = viewListViewModel,
                                addClicked = {
                                    if (viewListViewModel.updateTFState.isEmpty()) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Enter name for List",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@UpdateListDialog
                                    }
                                    scope.launch {
                                        val backColor = viewListViewModel.selectedColorState
                                        val textColor: Int = if (backColor in listOf(R.color.grey, R.color.yellow)) R.color.dark_black else R.color.white
                                        val category = Category(viewListViewModel.updateTFState, viewListViewModel.selectedColorState, textColor)
                                        category.id = viewListViewModel.category.id

                                        val updateCategory = databaseController.updateCategory(
                                            category,
                                            viewListViewModel.category.categoryName!!
                                        )
                                        if (!updateCategory) {
                                            Toast.makeText(context, "List with same name already exists", Toast.LENGTH_SHORT).show()
                                        }
                                        viewListViewModel.setDialogVisibilityState(false)
                                    }
                                })
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (fabViewModel.showListDialog && viewListViewModel.updateDialogVisibility) {
            fabViewModel.setShowListdialog(false)
            viewListViewModel.setDialogVisibilityState(false)
        } else {
            super.onBackPressed()
        }
    }
}