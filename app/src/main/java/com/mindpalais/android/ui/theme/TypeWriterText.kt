package com.mindpalais.android.ui.theme

import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    text: String,
    durationMillis: Int = text.length*10,
    delayMillis: Int = 0,
    textStyle: TextStyle = TextStyle.Default
) {
    var animatedText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        delay(delayMillis.toLong())
        text.forEachIndexed { index, _ ->
            animatedText = text.substring(0, index + 1)
            delay(durationMillis.toLong() / text.length)
        }
    }
    Text(
        text = animatedText,
        style = textStyle,
        color = Color.White
    )
}
