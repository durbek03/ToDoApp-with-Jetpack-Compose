package com.example.mvcexample.composableUi

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.viewmodels.MainViewModel
import com.example.mvcexample.R
import com.example.mvcexample.controller.DatabaseController
import com.example.mvcexample.globalui.*
import com.example.mvcexample.room.entity.Task
import com.example.mvcexample.viewmodels.AddTaskViewModel
import com.example.mvcexample.viewmodels.FabControlViewModel
import com.example.mvcexample.viewmodels.ViewListViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


class ViewListScreen(
    val viewModel: ViewListViewModel,
    val mainviewModel: MainViewModel,
    val fabViewModel: FabControlViewModel,
    val taskSelectChangedListener: OnTaskSelectChangedListener,
    val controller: DatabaseController
) {
    private val TAG = "ViewListScreen"

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun ViewListScreen(id: Int) {
        val uiController = rememberSystemUiController()
        fabViewModel.setFabvisibility(true)
        uiController.setStatusBarColor(Color.Black, darkIcons = false)

        val categoryList = mainviewModel.categoryList.value

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

        Log.d(TAG, "ViewListScreen: ${categoryWithTasks.category.categoryName}")

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
        if (categoryWithTasks.category.categoryName != null) {
            MainListScreen(categoryWithTasks = categoryWithTasks)
        }
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
                    if (viewModel.updateDialogVisibility) {
                        viewModel.setDialogVisibilityState(false)
                    }
                },
            shape = RoundedCornerShape(10.dp),
            color = colorResource(
                id = if (categoryWithTasks.category.backgroundColor != null) categoryWithTasks.category.backgroundColor!! else R.color.white
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Header(categoryWithTasks)
                Spacer(modifier = Modifier.height(40.dp))
                TaskList(
                    taskList = categoryWithTasks.taskList,
                    categoryWithTasks.category
                )
            }
        }
    }

    @Composable
    fun Header(categoryWithTask: CategoryWithTask) {
        val category = categoryWithTask.category
        val sizeText = categoryWithTask.taskList.size.formatTaskAmountText()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(63.dp, 40.dp, 20.dp, 0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    text = category.categoryName!!,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        id = category.textColor!!
                    )
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            viewModel.setcategory(category)
                            viewModel.setDialogVisibilityState(true)
                            fabViewModel.setShowListdialog(false)
                        },
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "",
                    tint = colorResource(
                        id = category.textColor!!
                    )
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = sizeText, color = colorResource(id = category.textColor!!))
        }
    }

    @Composable
    fun TaskList(taskList: List<Task>, category: Category) {
        LazyColumn() {
            itemsIndexed(taskList) { index, task ->
                ListTaskItem(task = task, category)
            }
        }
    }

    @Composable
    fun ListTaskItem(task: Task, category: Category) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50.dp))
                        .fillMaxWidth(0.08f)
                        .aspectRatio(1f)
                        .background(colorResource(id = category.backgroundColor!!))
                        .clickable {
                            taskSelectChangedListener.onTaskSelectChanged(task = task)
                        },
                    painter = painterResource(id = if (task.isDone!!) R.drawable.ic_marked else R.drawable.ic_unmarked),
                    contentDescription = "ic_tick",
                    tint = colorResource(id = if (task.isDone!!) R.color.blue else R.color.dark_grey)
                )
                Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text(
                        text = task.title!!,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = category.textColor!!)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (task.time!!.isNotEmpty()) {
                            ListTextWithIcon(
                                str = task.time!!,
                                icon = R.drawable.ic_alarm,
                                textColor = category.textColor!!
                            )
                        } else if (task.date!!.isNotEmpty()) {
                            ListTextWithIcon(
                                str = task.date!!,
                                icon = R.drawable.ic_calendar,
                                textColor = category.textColor!!
                            )
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
                Surface(
                    color = colorResource(id = category.backgroundColor!!),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.15f)
                        .aspectRatio(1f)
                ) {

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Divider(thickness = 0.5.dp, color = Color.Gray, startIndent = 63.dp)
    }

    @Composable
    fun ListTextWithIcon(str: String, icon: Int, modifier: Modifier = Modifier, textColor: Int) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            var text = str
            when {
                text.contains('.') -> {
                    if (text.checkIfToday()) text = "Today" else if (text.checkIfTomorrow()) text =
                        "Tomorrow"
                }
            }
            Icon(
                modifier = Modifier
                    .width(17.dp)
                    .height(17.dp),
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = colorResource(id = textColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text, color = colorResource(id = textColor))
        }
    }

    interface OnTaskSelectChangedListener {
        fun onTaskSelectChanged(task: Task)
    }
}