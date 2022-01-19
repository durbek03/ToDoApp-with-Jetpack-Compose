package com.example.mvcexample.composableUi

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
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
import com.example.mvcexample.room.entity.Category
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.room.entity.Task

class HomeScreen(val categoryList: List<CategoryWithTask>?, val taskList: List<Task>?) {

    @ExperimentalAnimationApi
    @Composable
    fun HomeScreen(modifier: Modifier = Modifier) {
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
                modifier = Modifier.aspectRatio(0.8f, matchHeightConstraintsFirst = true)
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
                CategoryItem(category.category, category.taskList.size)
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
                        .background(colorResource(id = R.color.white)),
                    painter = painterResource(id = R.drawable.ic_marked),
                    contentDescription = "ic_tick",
                    tint = colorResource(id = R.color.blue)
                )
                Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Text(text = task.title!!)
                    Text(text = "alarm")
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