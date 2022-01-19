package com.example.mvcexample.globalui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mvcexample.room.entity.Category

@Composable
fun CategoryItem(category: Category, taskCount: Int) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = colorResource(id = category.backgroundColor!!),
        modifier = Modifier
            .fillMaxWidth()
            .padding(63.dp, 0.dp, 10.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = category.categoryName!!)
            Text(text = taskCount.toString())
        }
    }
}