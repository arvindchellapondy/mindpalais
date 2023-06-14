package com.mindpalais.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCornerDivider(
    color: Color,
    cornerRadius: Dp,
    thickness: Dp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
//        Divider(
//            color = color,
//            thickness = thickness,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(thickness)
//        )
        Divider(
            color = whiteGray,
            thickness = thickness, startIndent = 0.dp,
            modifier = Modifier.fillMaxWidth().height(thickness)
        )
        Box(
            modifier = Modifier
                .size(cornerRadius)
                .background(color = color)
                .align(Alignment.Center)
        )
    }
}

