package com.mindpalais.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddItemDialog(showDialog: Boolean, onDismissRequest: () -> Unit,onAddItem: (String) -> Unit) {
    var textValue by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(370.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                TextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text("What would you like to schedule?") },
                    modifier = Modifier
                        .background(Color.White)
                        .height(300.dp)
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )

                )
                Row {
                    TextButton(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.Red
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Cancel, contentDescription = "Cancel")
//                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            showDialog = false
                            if (textValue.isNotBlank()) {
                                onAddItem(textValue)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lightBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
//                        Text("Add")
                    }

                    MicTextButton(
                        onClick = { /* do something */ },
                        text = "Speak"
                    )
                }
            }
        }
    }

}