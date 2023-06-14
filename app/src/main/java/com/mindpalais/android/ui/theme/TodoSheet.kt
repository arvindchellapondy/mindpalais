package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoSheet() {

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
//            MindpalaisTopAppBar({ getNoteDetailTopBar() })
        },
        content = {

//            Dialog(onDismissRequest = { showDialog = false }, content = {
//                Card(
//                    Modifier
//                        .padding(8.dp)
//                        .fillMaxWidth()
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .height(360.dp)
//                            .fillMaxWidth()
//                            .background(Color.White)
//                    ) {
//                        TextField(
//                            value = textValue,
//                            onValueChange = { textValue = it },
//                            placeholder = { Text("What would you like to schedule? ") },
//                            modifier = Modifier
//                                .background(Color.Transparent)
//                                .height(300.dp)
//                                .fillMaxWidth()
//                                .focusRequester(focusRequester),
//                            colors = TextFieldDefaults.textFieldColors(
//                                backgroundColor = Color.Transparent,
//                                focusedIndicatorColor = Color.Transparent,
//                                unfocusedIndicatorColor = Color.Transparent
//                            )
//                        )
//                        Row() {
//                            TextButton(
//                                onClick = {
//                                    // Do something with the entered text value here
//                                    showDialog = false
//                                }, colors = ButtonDefaults.textButtonColors(
//                                    backgroundColor = Color.Transparent,
//                                    contentColor = Color.Red // Change the text color to red
//                                ), modifier = Modifier.padding(8.dp)
//                            ) {
//                                Icon(Icons.Filled.Cancel, contentDescription = "Cancel")
//                                //Text("Cancel")
//                            }
//                            TextButton(
//                                onClick = {
//                                    showDialog = false
//                                    if (textValue.isNotBlank()) {
//                                        mindMapViewModel.sendMessageToAssistant(textValue)
//                                    }
//                                    android.widget.Toast.makeText(
//                                        context, "Adding item...", android.widget.Toast.LENGTH_SHORT
//                                    ).show()
//                                    textValue = ""
//                                }, colors = ButtonDefaults.buttonColors(
//                                    backgroundColor = lightBlue, contentColor = Color.White
//                                ), modifier = Modifier.padding(8.dp)
//                            ) {
//                                Icon(Icons.Filled.Add, contentDescription = "Add")
////                            Text("Add")
//                            }
//
//                            MicTextButton(
//                                onClick = { /* do something */ }, text = ""
//                            )
//                        }
//
//                    }
//                }
//            })
        }
    )
}