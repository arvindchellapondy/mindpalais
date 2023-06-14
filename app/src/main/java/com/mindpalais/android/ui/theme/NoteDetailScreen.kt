package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mindpalais.android.model.MindMapViewModel

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun NoteDetailScreen(viewModel: MindMapViewModel) {
    val scaffoldState = rememberScaffoldState()

}

@Composable
fun getNoteDetailTopBar() {
    Row() {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        Text(
            text = "Notes", fontFamily = interFontFamily, color = Color.White,
            fontWeight = FontWeight.Bold, fontSize = 18.sp
        )
    }

}