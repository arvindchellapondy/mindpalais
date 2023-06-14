package com.mindpalais.android.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Toast(message: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        color = MaterialTheme.colors.onSurface
    ) {
        Box(
            Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                message,
                style = TextStyle(
                    color = lightBlue,
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}