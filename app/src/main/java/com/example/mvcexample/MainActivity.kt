package com.example.mvcexample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.mvcexample.composables.FabDialog
import com.example.mvcexample.controller.Controller
import com.example.mvcexample.home.HomeScreen
import com.example.mvcexample.room.database.AppDatabase
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task
import com.example.mvcexample.ui.theme.MVCexampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var controller: Controller

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVCexampleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    var addCategoryDialogState by remember {
                        mutableStateOf(false)
                    }

                    var categoryList by remember {
                        mutableStateOf(listOf<CategoryWithTask>())
                    }
                    var taskList by remember {
                        mutableStateOf(listOf<Task>())
                    }

                    val interactionSource = remember { MutableInteractionSource() }

                    var fabState by remember {
                        mutableStateOf(FabState.NOTROTATED)
                    }

                    var showFabDialog by remember {
                        mutableStateOf(false)
                    }

                    val homeScreen = HomeScreen(categoryList, taskList)
                    homeScreen.HomeScreen(modifier = Modifier
                        .clickable(indication = null, interactionSource = interactionSource) {
                            if (fabState == FabState.ROTATED) fabState = FabState.NOTROTATED
                        }
                    )

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                        AnimatedVisibility(visible = showFabDialog) {
                            FabDialog({
                                fabState = FabState.NOTROTATED
                            }, {

                                fabState = FabState.NOTROTATED
                            })
                        }

                        var backColorState by remember {
                            mutableStateOf(value = Color.White)
                        }
                        val animateBackColor = animateColorAsState(targetValue = backColorState)
                        var iconColor by remember {
                            mutableStateOf(value = Color(0xFF006CFF))
                        }
                        val animateIconColor = animateColorAsState(targetValue = iconColor)
                        var rotationState by remember {
                            mutableStateOf(0f)
                        }
                        val animateRotation = animateFloatAsState(targetValue = rotationState)

                        LaunchedEffect(key1 = fabState) {
                            if (fabState == FabState.ROTATED) {
                                backColorState = Color(0xFF006CFF)
                                iconColor = Color.White
                                rotationState = 45f
                                showFabDialog = true
                            } else {
                                backColorState = Color.White
                                iconColor = Color(0xFF006CFF)
                                rotationState = 0f
                                showFabDialog = false
                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                fabState =
                                    if (fabState == FabState.ROTATED) FabState.NOTROTATED else FabState.ROTATED
                            },
                            backgroundColor = animateBackColor.value,
                            modifier = Modifier
                                .offset((-20).dp, (-20).dp)
                                .rotate(animateRotation.value)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = "ic_add",
                                tint = animateIconColor.value
                            )
                        }
                    }

                    lifecycleScope.launch(Dispatchers.IO) {
                        controller = Controller(AppDatabase.getInstance(this@MainActivity))
                        val allTasks = controller.getAllTasks()
                        val categoryWithTask = controller.getCategoryWithTask()
                        categoryList = categoryWithTask
                        taskList = allTasks
                    }
                }
            }
        }
    }
}