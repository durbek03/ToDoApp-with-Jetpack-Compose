package com.example.mvcexample.globalui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.mvcexample.viewmodels.FabControlViewModel
import com.example.mvcexample.R

@Composable
fun ListDialog(
    modifier: Modifier = Modifier,
    viewModel: FabControlViewModel,
    addClicked: () -> Unit
) {
    val colors = listOf(
        R.color.black,
        R.color.blue,
        R.color.purple,
        R.color.yellow,
        R.color.red,
        R.color.green,
        R.color.grey
    )
    LaunchedEffect(true) {
        viewModel.setSelectedColorstate(R.color.black)
        viewModel.setlistDialogTFState("")
    }
    Surface(
        modifier = modifier
            .fillMaxWidth(0.75f),
        color = Color.White,
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp, 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel.listDialogTextFieldState,
                onValueChange = {
                    viewModel.setlistDialogTFState(it)
                },
                label = {
                    Text(text = "Enter name for List")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = colorResource(id = R.color.blue),
                    focusedLabelColor = colorResource(id = R.color.blue),
                    focusedBorderColor = colorResource(id = R.color.blue),
                    cursorColor = colorResource(id = R.color.blue)
                )
            )
            Spacer(modifier = Modifier.height(15.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                itemsIndexed(colors) { index, color ->
                    ColorPickerItem(color = color, viewModel = viewModel)
                }
            }

            Spacer(modifier = Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = {
                        addClicked.invoke()
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(
                            viewModel.selectedColorState
                        )
                    )
                ) {
                    Text(text = "Add", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ColorPickerItem(color: Int, viewModel: FabControlViewModel) {
    Surface(
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .clickable {
                viewModel.setSelectedColorstate(color = color)
            },
        shape = RoundedCornerShape(50.dp),
        color = colorResource(id = color),
    ) {
    }
}