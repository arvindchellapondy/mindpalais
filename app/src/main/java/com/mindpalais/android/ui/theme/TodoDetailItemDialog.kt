package com.mindpalais.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mindpalais.android.model.ResponseModel

@Composable
fun CustomDialog(
    response: ResponseModel?,
    onSaveClick: (ResponseModel) -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit
) {
//    Dialog(
//        onDismissRequest = onCancelClick,
//        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Transparent)
                    .padding(bottom = 60.dp)
            ){
                TodoDetailItem(
                    response = response,
                    onSaveClick = onSaveClick,
                    onDeleteClick = onDeleteClick,
                    onCancelClick = onCancelClick
                )
            }

//        }
//    )
}
