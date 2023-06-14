package com.mindpalais.android.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDeleteRow(
    text: String,
    onDismiss: () -> Unit
) {
//    var dismissState by remember { mutableStateOf(DismissState) }
//
//    SwipeToDismiss(
//        state = dismissState,
//        onDismiss = {
//            dismissState = it
//            onDismiss()
//        },
//        directions = setOf(SwipeDirection.StartToEnd),
//        background = {
//            val color by animateColorAsState(
//                when (dismissState) {
//                    SwipeToDismissValue.Default -> Color.Transparent
//                    SwipeToDismissValue.DismissedToEnd -> Color.Red
//                    SwipeToDismissValue.DismissedToStart -> Color.Transparent
//                }
//            )
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                backgroundColor = color
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = "Delete",
//                    tint = Color.White,
//                    modifier = Modifier.align(
//                        Alignment.CenterEnd
//                    ).padding(end = 16.dp)
//                )
//            }
//        },
//        dismissContent = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp, horizontal = 16.dp)
//                    .height(48.dp)
//                    .background(Color.White)
//            ) {
//                Text(text = text)
//            }
//        }
//    ) { state ->
//        dismissState = state
//    }
}
