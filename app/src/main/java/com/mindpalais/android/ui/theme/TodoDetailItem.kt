package com.mindpalais.android.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalais.android.model.ResponseModel
import com.mindpalais.android.model.TaskStatus

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoDetailItem(
    response: ResponseModel?,
    onSaveClick: (ResponseModel) -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    var idValue by remember { mutableStateOf(response?.id ?: "") }
    var headingValue by remember { mutableStateOf(response?.action ?: "") }
    var messageValue by remember { mutableStateOf(response?.message ?: "") }
    var createdValue by remember { mutableStateOf(response?.created ?: "") }
    var peopleValue by remember { mutableStateOf(response?.people?.joinToString(", ") ?: "") }
    var dateValue by remember { mutableStateOf(response?.date ?: "") }
    var statusValue by remember { mutableStateOf(response?.status ?: "Ready") }

    Column(modifier = Modifier.padding(24.dp).background(Color.Transparent)) {
        Text(text = "Action", fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal, fontSize = 12.sp, color = Color.Gray)
        BasicTextField(
            value = headingValue,
            onValueChange = { newText ->
                headingValue = newText
            },
                        textStyle = TextStyle(fontFamily = interFontFamily,
                fontWeight = FontWeight.Bold, fontSize = 16.sp, color = darkPrimary),
        )
//        Divider(
//            color = lightPrimary,
//            thickness = 1.dp,startIndent=12.dp,
//            modifier = Modifier.padding(vertical = 16.dp, horizontal = 0.dp)
//        )

//        TextField(
//            value = headingValue,
//            textStyle = TextStyle(fontFamily = interFontFamily,
//                fontWeight = FontWeight.Bold, fontSize = 20.sp, color = darkPrimary),
//            onValueChange = { headingValue = it },
//            modifier = Modifier.fillMaxWidth().padding(0.dp),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color.Red,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
//        )
//        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            color = whiteGray,
            thickness = 1.dp,startIndent=2.dp,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )
        Text(text = "Message", fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal, fontSize = 12.sp, color = Color.Gray)
        BasicTextField(
            value = messageValue,
            onValueChange = { newText ->
                messageValue = newText
            },
            textStyle = TextStyle(fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal, fontSize = 16.sp, color = darkPrimary),
        )
//        TextField(
//            value = messageValue,
//            onValueChange = { messageValue = it },
//            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
//        )
//        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            color = whiteGray,
            thickness = 1.dp,startIndent=2.dp,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )
        Text(text = "Due on", fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal, fontSize = 12.sp, color = Color.Gray)
        BasicTextField(
            value = dateValue,
            onValueChange = { newText ->
                dateValue = newText
            },
            textStyle = TextStyle(fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal, fontSize = 16.sp, color = darkPrimary),
        )
//        OutlinedTextField(
//            value = createdValue,
//            onValueChange = { createdValue = it },
//            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Date to be completed")
//        OutlinedTextField(
//            value = dateValue,
//            onValueChange = { dateValue = it },
//            modifier = Modifier.fillMaxWidth(),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        Text("Status")
//        RadioGroup(
//            options = listOf("Ready", "In Progress", "Blocked", "Completed"),
//            selectedOption = statusValue,
//            onOptionSelected = { statusValue = it }
//        )

        Divider(
            color = whiteGray,
            thickness = 1.dp,startIndent=2.dp,
            modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
        )
        Text(text = "Status", fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal, fontSize = 12.sp, color = Color.Gray)
        VerticalRadioGroup(
            options = listOf(TaskStatus.READY, TaskStatus.IN_PROGRESS, TaskStatus.BLOCKED, TaskStatus.COMPLETE),
            selectedOption = statusValue,
            onOptionSelected = { statusValue = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
//        Divider(
//            color = lightPrimary,
//            thickness = 1.dp,startIndent=2.dp,
//            modifier = Modifier.padding(vertical = 10.dp, horizontal = 0.dp)
//        )
        Row {
//            Button(onClick = { onCancelClick() }, modifier = Modifier.weight(1f)) {
//                Text("Cancel")
//            }
            TextButton(
                onClick = {
                    onCancelClick()
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = darkPrimary // Change the text color to red
                ),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(Icons.Outlined.Cancel, contentDescription = "Cancel")
//                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(
                onClick = {
                    onSaveClick(
                        ResponseModel(
                            id = idValue,
                            action = headingValue,
                            message = messageValue,
                            created = createdValue,
                            date = dateValue,
                            people = peopleValue.split(",").map { it.trim() }.toTypedArray(),
                            status = statusValue
                        )
                    )
                },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = lightBlue,
                    contentColor = Color.White // Change the text color to red
                ),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(Icons.Filled.Save, contentDescription = "Save")
//                Text("Save")
            }
//            Button(onClick = {
//                onSaveClick(
//                    ResponseModel(
//                        action = headingValue,
//                        message = messageValue,
//                        created = createdValue,
//                        date = dateValue,
//                        people = peopleValue.split(",").map { it.trim() }.toTypedArray(),
//                        status = statusValue
//                    )
//                )
//            }, modifier = Modifier.weight(1f)) {
//                Text("Save")
//            }
            Spacer(modifier = Modifier.width(16.dp))
//            Button(onClick = { onDeleteClick() }, modifier = Modifier.weight(1f)) {
//                Text("Delete")
//            }

            TextButton(
                onClick = { onDeleteClick() },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.Red // Change the text color to red
                ),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete")
//                Text("Delete")
            }
        }
    }
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row {
        options.forEach { option ->
            Row(
                Modifier
                    .clickable(onClick = { onOptionSelected(option) })
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
