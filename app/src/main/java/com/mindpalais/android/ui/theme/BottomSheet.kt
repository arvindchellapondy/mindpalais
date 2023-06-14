package com.mindpalais.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetState: BottomSheetState,
    sheetHeight: Dp,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = sheetContent,
        sheetShape = Shapes.medium,
        sheetElevation = 8.dp,
        sheetBackgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .clickable(
                    onClick = {
                        if (modalBottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
                            scope.launch {
                                modalBottomSheetState.show()
                            }
                        }
                    }
                )
        ) {
            // Handle gestures or interactions inside the bottom sheet
        }
    }
}

@Composable
fun BottomSheetTopAppBar(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp), // Set the desired shape for the shadow
        color = lightPrimary // Set the desired background color of the Surface
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
                Divider(
                    color = Color(0xAA200A50),
                    thickness = 2.dp, startIndent = 0.dp,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 168.dp)
                )
                TopAppBar(

                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    contentColor = Color.White,
                    contentPadding = PaddingValues(16.dp, 0.dp, 16.dp, 0.dp),
                ) {
                    Column() {


                        content()
                    }
                }
                Divider(
                    color = Color(0xAA200A50),
                    thickness = 2.dp, startIndent = 0.dp,
                )

            }


        }
    }
}