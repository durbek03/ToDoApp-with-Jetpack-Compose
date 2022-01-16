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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
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
                    var categoryList by remember {
                        mutableStateOf(listOf<CategoryWithTask>())
                    }
                    var taskList by remember {
                        mutableStateOf(listOf<Task>())
                    }
                    HomeScreen(categoryList, taskList).HomeScreen()
                    FabWithDialog()

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

    @ExperimentalAnimationApi
    @Composable
    fun FabWithDialog() {
        var showFabDialog by remember {
            mutableStateOf(false)
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            AnimatedVisibility(visible = showFabDialog) {
                FabDialog()
            }
            var fabState by remember {
                mutableStateOf(FabState.NOTROTATED)
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
                } else {
                    backColorState = Color.White
                    iconColor = Color(0xFF006CFF)
                    rotationState = 0f
                }
            }

            FloatingActionButton(
                onClick = {
                    showFabDialog = !showFabDialog
                    fabState = if (showFabDialog) {
                        FabState.ROTATED
                    } else FabState.NOTROTATED
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
    }

    @Composable
    fun FabDialog() {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(2f)
                .offset((-20).dp, (-100).dp),
            elevation = 5.dp,
            shape = RoundedCornerShape(5.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                    }) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_task),
                        contentDescription = "ic_task",
                        tint = colorResource(id = R.color.blue)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Task", color = colorResource(id = R.color.blue))
                }
                Divider(thickness = 1.dp, color = Color.Gray)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                    }) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_category),
                        contentDescription = "ic_category",
                        tint = colorResource(id = R.color.blue)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "List", color = colorResource(id = R.color.blue))
                }
            }
        }
    }
}