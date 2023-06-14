package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.model.Note
import com.mindpalais.android.model.UiViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddNoteScreen(mindMapViewModel: MindMapViewModel, uiViewModel: UiViewModel) {
//    val databaseHelper = DatabaseHelper(LocalContext.current)

    val noteVal = remember { mutableStateOf("") }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
//            BottomSheetTopAppBar({ getAddNotesTopBar() })
        },
        content = {

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Add a Note", fontFamily = interFontFamily, color = darkPrimary,
                    fontWeight = FontWeight.Bold, fontSize = 18.sp
                )
                Card(
                    Modifier
                        .padding(vertical = 12.dp, horizontal = 4.dp)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        RoundedTextField(noteVal)

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            TextButton(
                                onClick = {
                                    val note = Note(
                                        id = generateUniqueId(),
                                        createdAt = getCurrentDateTime(),
                                        note = noteVal.value,
                                        title = "",
                                        generated = arrayOf(),
                                        wordcloud = arrayOf()
                                    )

                                    mindMapViewModel.insertNote(note)
                                    uiViewModel.setExpandBottomSheet(false)
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = darkPrimary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Save,
                                    contentDescription = "Save",
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            TextButton(
                                onClick = { },
                                colors = ButtonDefaults.textButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = darkPrimary
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.Cancel,
                                    contentDescription = "Cancel",
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }



            }


        }
    )
}

@Composable
fun RoundedTextField(note: MutableState<String>) {


    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
        modifier = Modifier.padding(8.dp)
    ) {
        BasicTextField(
            value = note.value,
            onValueChange = { note.value = it },
            modifier = Modifier
                .padding(4.dp)
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(240.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 28.sp
            )
        )
    }
}

@Composable
fun RoundedButton() {
    Button(
        onClick = { /* Button click action */ },
        modifier = Modifier
            .padding(16.dp)
            .defaultMinSize(minWidth = 100.dp)
            .background(
                color = darkPrimary,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = Color.White
        )
    ) {
        Text(text = "Save")
    }
}

@Composable
fun getAddNotesTopBar() {
    Column() {
//                            Divider(
//                                color = whiteGray,
//                                thickness = 4.dp, startIndent = 0.dp,
//                                modifier = Modifier.padding(180.dp, 6.dp, 180.dp, 12.dp)
//                            )
//        Text(
//            text = "Add a Note", fontFamily = interFontFamily, color = Color.White,
//            fontWeight = FontWeight.Bold, fontSize = 18.sp
//        )
    }
}

fun generateUniqueId(): String {
    return Instant.now().toEpochMilli().toString()
}

fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return currentDateTime.format(formatter)
}