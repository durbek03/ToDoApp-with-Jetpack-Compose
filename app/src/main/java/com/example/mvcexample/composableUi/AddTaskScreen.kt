package com.example.mvcexample.composableUi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mvcexample.R
import com.example.mvcexample.globalui.formatTaskAmountText
import com.example.mvcexample.room.entity.CategoryWithTask
import com.example.mvcexample.viewmodels.AddTaskViewModel
import com.example.mvcexample.viewmodels.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.*

@ExperimentalAnimationApi
class AddTaskScreen(
    val viewModel: AddTaskViewModel,
    val navController: NavController,
    val mainViewModel: MainViewModel,
    val taskAdded: () -> Unit
) {
    private val TAG = "AddTaskScreen"

    @Composable
    fun AddTaskScreen() {
        val uiController = rememberSystemUiController()
        uiController.setStatusBarColor(Color.White, darkIcons = true)
        LaunchedEffect(true) {
            viewModel.setTextFieldState("")
            viewModel.setdate("")
            viewModel.settime("")
            viewModel.setcategory(null)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopButtons()
                Spacer(modifier = Modifier.height(20.dp))
                TaskDescription()
                Divider(thickness = 1.dp, color = Color.Gray)
                TabLayout()
                Spacer(modifier = Modifier.height(5.dp))
                CategoryList()
            }
        }
    }

    @Composable
    fun TopButtons() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cancel",
                color = colorResource(id = R.color.blue),
                fontSize = 17.sp,
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = "Done",
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.blue),
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    taskAdded.invoke()
                }
            )
        }
    }

    @Composable
    fun TaskDescription(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .padding(20.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_unmarked),
                    contentDescription = "unmarked",
                )
                Column(modifier = Modifier.fillMaxWidth(0.8f)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(1f),
                            textStyle = TextStyle(color = Color.Black, fontSize = 17.sp),
                            value = viewModel.taskTextFieldState,
                            onValueChange = { viewModel.setTextFieldState(it) },
                            placeholder = {
                                Text(
                                    text = "What do you want to do?",
                                    color = Color.Gray,
                                    fontSize = 17.sp
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                cursorColor = Color.Gray,
                                backgroundColor = Color.Transparent,
                                placeholderColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Row(modifier = Modifier.padding(13.dp, 0.dp, 0.dp, 0.dp)) {
                        AnimatedVisibility(visible = viewModel.date.isNotEmpty()) {
                            textWithIcon(str = viewModel.date, icon = R.drawable.ic_calendar)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        AnimatedVisibility(visible = viewModel.time.isNotEmpty()) {
                            textWithIcon(str = viewModel.time, icon = R.drawable.ic_alarm)
                        }
                    }
                }
                Surface(
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp),
                    shape = RoundedCornerShape(50.dp),
                    color = colorResource(
                        id = viewModel.pickedCategoryColor.value
                    )
                ) {

                }
            }
        }
    }

    @Composable
    fun TabLayout() {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp, 20.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    tint = colorResource(id = R.color.blue),
                    contentDescription = "",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .clickable {
                            showCalendar(context)
                        }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_alarm),
                    tint = colorResource(id = R.color.blue),
                    contentDescription = "",
                    modifier = Modifier
                        .width(25.dp)
                        .height(25.dp)
                        .clickable {
                            showAlarm(context)
                        }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Inbox",
                    color = colorResource(id = R.color.blue)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    modifier = Modifier
                        .width(10.dp)
                        .height(10.dp),
                    shape = RoundedCornerShape(50.dp),
                    color = colorResource(id = viewModel.pickedCategoryColor.value)
                ) {}
            }
        }
    }

    fun showCalendar(context: Context) {
        val calendar = Calendar.getInstance()
        val calyear = calendar.get(Calendar.YEAR)
        val calmonth = calendar.get(Calendar.MONTH)
        val calday = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { p0, p1, p2, p3 ->
                val month = String.format("%02d", p2+1)
                val day = String.format("%02d", p3)
                val date = "$day.$month.$p1"
                viewModel.setdate(date)
            }, calyear, calmonth, calday
        )
        datePickerDialog.show()
    }

    fun showAlarm(context: Context) {
        if (viewModel.date.isEmpty()) {
            Toast.makeText(context, "Pick date before picking time", Toast.LENGTH_SHORT).show()
            return
        }
        val dialog = TimePickerDialog(
            context,
            { p0, p1, p2 ->
                val hour = String.format("%02d", p1)
                val minute = String.format("%02d", p2)
                val time = "$hour:$minute"
                viewModel.settime(time)
            }, 0, 0, true
        )
        dialog.show()
    }

    @Composable
    fun CategoryList() {
        val categoryList = mainViewModel.categoryList.observeAsState()
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        ) {
            itemsIndexed(items = categoryList.value!!) { index, category ->
                Category(category = category)
            }
        }
    }

    @Composable
    fun Category(category: CategoryWithTask) {
        val sizeText = category.taskList.size.formatTaskAmountText()
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 7.dp)
                .clickable { viewModel.setcategory(category.category) },
            shape = RoundedCornerShape(10.dp),
            color = colorResource(
                id = category.category.backgroundColor!!
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                ) {
                    Text(text = category.category.categoryName!!, color = colorResource(id = category.category.textColor!!), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = sizeText, color = colorResource(id = category.category.textColor!!))
                }
                AnimatedVisibility(visible = viewModel.category?.id == category.category.id) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_list_checked),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color.White),
                        contentDescription = "",
                        tint = colorResource(id = R.color.blue)
                    )
                }
            }
        }
    }

    @Composable
    fun textWithIcon(str: String, icon: Int, modifier: Modifier = Modifier) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = icon), contentDescription = "", tint = Color.Gray)
            Text(text = str, color = Color.Gray)
        }
    }
}