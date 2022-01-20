package com.example.mvcexample.globalui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvcexample.room.entity.Category

@Composable
fun CategoryItem(category: Category, taskCount: Int, modifier: Modifier = Modifier) {
    val sizeText = taskCount.formatTaskAmountText()

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = colorResource(id = category.backgroundColor!!),
        modifier = modifier
            .fillMaxWidth()
            .padding(63.dp, 0.dp, 10.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(text = category.categoryName!!, color = colorResource(category.textColor!!), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = sizeText, color = colorResource(category.textColor!!))
        }
    }
}