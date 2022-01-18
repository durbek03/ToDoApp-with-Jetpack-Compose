package com.example.mvcexample.globalui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mvcexample.FabControlViewModel
import com.example.mvcexample.MainViewModel
import com.example.mvcexample.R

@ExperimentalAnimationApi
@Composable
fun CustomFloatingActionButton(viewModel: FabControlViewModel) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        AnimatedVisibility(visible = viewModel.showFabDialog) {
            FabDialog({
                viewModel.setFabstate(FabState.NOTROTATED)
                viewModel.setShowListdialog(true)
            }, {
                viewModel.setFabstate(FabState.NOTROTATED)

            }, modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(2f)
                .offset((-20).dp, (-100).dp)
            )
        }

        val animateBackColor = animateColorAsState(targetValue = viewModel.backColorState)
        val animateIconColor = animateColorAsState(targetValue = viewModel.iconColor)
        val animateRotation = animateFloatAsState(targetValue = viewModel.rotationState)

        LaunchedEffect(key1 = viewModel.fabState) {
            if (viewModel.fabState == FabState.ROTATED) {
                viewModel.setFabBackColor(Color(0xFF006CFF))
                viewModel.setFabIconColor(Color.White)
                viewModel.setRotationstate(45f)
                viewModel.setShowFabdialog(true)
            } else {
                viewModel.setFabBackColor(Color.White)
                viewModel.setFabIconColor(Color(0xFF006CFF))
                viewModel.setRotationstate(0f)
                viewModel.setShowFabdialog(false)
            }
        }

        androidx.compose.material.FloatingActionButton(
            onClick = {
                viewModel.setFabstate(if (viewModel.fabState == FabState.ROTATED) FabState.NOTROTATED else FabState.ROTATED)
                if (viewModel.showListDialog) viewModel.setShowListdialog(false)
            },
            backgroundColor = animateBackColor.value,
            modifier = Modifier
                .align(Alignment.BottomEnd)
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