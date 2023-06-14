package com.mindpalais.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun MindpalaisTopAppBar(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
//        elevation = 8.dp, // Set the desired shadow elevation
        shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp), // Set the desired shape for the shadow
//        shape = RectangleShape,
        color = darkPrimary2 // Set the desired background color of the Surface

    )
    {
        Box(
            modifier = Modifier
                .padding(top = 0.dp, start = 0.dp, end = 0.dp)
                .fillMaxWidth()
//        .background(Color.Magenta, shape = RoundedCornerShape(16.dp))
//        .background(
//        brush = Brush.verticalGradient(
//            colors = listOf(darkPrimary, lightPrimary), // Set your desired gradient colors
//            startY = 170f,
//            endY = Float.POSITIVE_INFINITY
//        )
//    )
                .background(darkPrimary2)
                .shadow(elevation = 0.dp)

        )
        {
            Column() {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    contentColor = Color.White,
                    contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 0.dp),
                ) {
                    content()
                }
                Divider(
                    color = Color(0xAA200A50),
                    thickness = 2.dp, startIndent = 0.dp,
                )

            }


        }
    }
}
