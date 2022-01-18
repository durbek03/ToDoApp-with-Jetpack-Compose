package com.example.mvcexample.globalui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mvcexample.R


@Composable
fun FabDialog(taskPicked: () -> Unit, listPicked: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        elevation = 3.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    taskPicked.invoke()
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
                    listPicked.invoke()
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