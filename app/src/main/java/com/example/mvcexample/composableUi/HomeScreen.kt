package com.example.mvcexample.composableUi

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvcexample.R
import com.example.mvcexample.globalui.CategoryItem
import com.example.mvcexample.globalui.checkIfToday
import com.example.mvcexample.globalui.checkIfTomorrow
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

class HomeScreen(
    val categoryList: List<CategoryWithTask>?,
    val taskList: List<Task>?,
    val listTaskSelection: ListTaskSelection
) {

    @ExperimentalAnimationApi
    @Composable
    fun HomeScreen(modifier: Modifier = Modifier) {
        val uiController = rememberSystemUiController()
        uiController.setStatusBarColor(Color.White, darkIcons = true)
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            TopAppBar()
            if (!categoryList.isNullOrEmpty()) {
                TodayTasks()
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(0.dp, 150.dp),
                    text = "No Tasks for Today",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue)
                )
            }
        }
    }

    @Composable
    fun TopAppBar() {
        Row(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .padding(63.dp, 0.dp, 10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Today",
                color = Color.Black,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = "ic_more",
                tint = colorResource(
                    id = R.color.blue
                ),
                modifier = Modifier.aspectRatio(0.6f, matchHeightConstraintsFirst = true)
            )
        }
    }

    @Composable
    fun TodayTasks() {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            if (taskList != null) {
                itemsIndexed(taskList) { index, task ->
                    TaskItem(task)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Lists",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(63.dp, 0.dp, 0.dp, 0.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            itemsIndexed(categoryList!!) { index, category ->
                CategoryItem(
                    category.category,
                    category.taskList.size,
                    modifier = Modifier.clickable {
                        listTaskSelection.onListSelected(category)
                    })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    @Composable
    fun TaskItem(task: Task) {
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
                        .background(colorResource(id = R.color.white))
                        .clickable {
                            listTaskSelection.onTaskSelectedChanged(task = task)
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
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (task.time!!.isNotEmpty()) {
                            TextWithIcon(str = task.time!!, icon = R.drawable.ic_alarm)
                        } else if (task.date!!.isNotEmpty()) {
                            TextWithIcon(str = task.date!!, icon = R.drawable.ic_calendar)
                        } else {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
                Surface(
                    color = colorResource(id = task.color!!),
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
}

@Composable
fun TextWithIcon(str: String, icon: Int, modifier: Modifier = Modifier) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        var text = str
        when {
            text.contains('.') -> {
                if (text.checkIfToday()) text = "Today" else if (text.checkIfTomorrow()) text = "Tomorrow"
            }
        }
        Icon(
            modifier = Modifier
                .width(17.dp)
                .height(17.dp),
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = text, color = Color.Gray)
    }
}

interface ListTaskSelection {
    fun onTaskSelectedChanged(task: Task)
    fun onListSelected(category: CategoryWithTask)
}

