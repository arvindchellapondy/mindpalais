@file:OptIn(ExperimentalFoundationApi::class)

package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mindpalais.android.R
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.model.ResponseModel
import com.mindpalais.android.model.TaskStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.zIndex
import com.mindpalais.android.model.UiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun TodoScreen(
    viewModel: MindMapViewModel,
    uiViewModel: UiViewModel
) {
    val items = remember { mutableStateListOf<ResponseModel>() }
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }
    val pages = listOf("Past Due", "Right Now", "Later")
    val pagerState = rememberPagerState(0)

    val bottomSheetState = rememberBottomSheetScaffoldState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val primaryDarkBlue = colorResource(id = R.color.dark_primary_blue)

    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(
            ResponseModel(
                "",
                "",
                "",
                "",
                arrayOf(),
                "",
                ""
            )
        )
    }

    val onItemClick: (ResponseModel) -> Unit = { sItem ->
        selectedItem = sItem
        showDialog = true

        uiViewModel.setBottomSheetContent {
            getBottomSheetContent(selectedItem,showDialog)
            uiViewModel.setExpandBottomSheet(true)
        }
    }

    val tabItems = mutableMapOf(
        "Right Now" to mutableListOf<ResponseModel>(),
        "Later" to mutableListOf<ResponseModel>(),
        "Past Due" to mutableListOf<ResponseModel>()
    )

    val responses = viewModel.responses.collectAsState(initial = emptyList()).value
    val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    responses.forEach { response ->
        response.date?.let {

            when (getDaysBetweenDates(currentDate, response.date)) {
                in Long.MIN_VALUE..-1 -> {
                    tabItems["Past Due"]?.add(response)
                }
                0L -> {
                    tabItems["Right Now"]?.add(response)
                }
                in 1..Long.MAX_VALUE -> {
                    tabItems["Later"]?.add(response)
                }
            }
        }
    }

    val expandBottomSheet by uiViewModel.expandBottomSheet.collectAsState()

    LaunchedEffect(expandBottomSheet) {
        if (expandBottomSheet) {
            Log.e("line 83", "CalScreen expandBottomSheet - visible")

        } else {
            Log.e("line 85", "CalScreen expandBottomSheet - hidden")
            //showDialog = false

        }
    }

    val sheetContent: @Composable ColumnScope.() -> Unit = {
        // Content of the bottom sheet
        Column(
            modifier = Modifier.padding(0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(text = "Bottom Sheet Content")
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = { /* Perform collapse operation */ }) {
//                Text(text = "Close")
//            }

            if (showDialog) {

                CustomDialog(
                    response = selectedItem,
                    onSaveClick = { updatedResponse ->
                        viewModel.updateResponse(updatedResponse)
                        showDialog = false
                    },
                    onDeleteClick = {
                        viewModel.deleteResponse(selectedItem)
                        showDialog = false
                    },
                    onCancelClick = { showDialog = false }
                )

                //  CalendarScreen(viewModel = viewModel)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {

            MindpalaisTopAppBar({ getTodoTopAppBar() })

        },
        content = {

            Column {
                TabRow(selectedTabIndex = selectedTab, backgroundColor = Color.Transparent) {
                    pages.forEachIndexed { index, text ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { setSelectedTab(index) },
                            text = {
                                Text(
                                    text, color = darkPrimary,
                                    fontFamily = interFontFamily,
                                    fontWeight = if (selectedTab == index) FontWeight.Normal else FontWeight.Normal,
                                    fontSize = 16.sp
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    pageCount = pages.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    TodoList(items = tabItems[pages[page]] ?: emptyList(), onItemClick)
                    if (showDialog) {
                        coroutineScope.launch { bottomSheetState.bottomSheetState.expand() }
//                            CustomDialog(
//                                response = selectedItem,
//                                onSaveClick = { updatedResponse ->
//                                    viewModel.updateResponse(updatedResponse)
//                                    showDialog = false
//                                },
//                                onDeleteClick = {
//                                    viewModel.deleteResponse(selectedItem)
//                                    showDialog = false
//                                },
//                                onCancelClick = { showDialog = false }
//                            )

                    }
                }
            }
        }
    )

    LaunchedEffect(selectedTab) {
        pagerState.scrollToPage(selectedTab)
    }

}

@Composable
fun getTodoTopAppBar() {
    Text(
        text = "To-do", fontFamily = interFontFamily, color = Color.White,
        fontWeight = FontWeight.Bold, fontSize = 18.sp
    )
}


@Composable
fun TodoList(items: List<ResponseModel>, showDialog: (ResponseModel) -> Unit) {
    Card(
        Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        LazyColumn {
            items(items) { item ->
//                Log.e("Item",item.message);
//                Log.e("Dates",item.date.toString());
                TodoItem(item = item, showDialog)
                if (item != items.last()) {
                    Divider(
                        color = whiteGray,
                        thickness = 1.dp, startIndent = 12.dp,
                        modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun TodoItem(item: ResponseModel, showDialog: (ResponseModel) -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable(
                MutableInteractionSource(),
                indication = null,
                onClick = { showDialog(item) }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconTint = when (item.status) {
            TaskStatus.COMPLETE -> darkGreen
            TaskStatus.IN_PROGRESS -> orange
            TaskStatus.BLOCKED -> Color.Red
            TaskStatus.READY -> darkBlue
            else -> {
                darkBlue
            }
        }

        item.action?.let {
            Text(
                it,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.DarkGray,
                lineHeight = 24.sp,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f),
                textDecoration = if (item.status == TaskStatus.COMPLETE) TextDecoration.LineThrough else TextDecoration.None
            )
        } ?: Text(
            item.message,
            fontFamily = interFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray,
            lineHeight = 24.sp,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(2f),
            textDecoration = if (item.status == TaskStatus.COMPLETE) TextDecoration.LineThrough else TextDecoration.None
        )
//            Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = iconTint
        )

    }
}


val interFontFamily = FontFamily(
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_semibold, FontWeight.Bold),
    Font(R.font.comfortaa_regular, FontWeight.Medium),
    Font(R.font.constance, FontWeight.SemiBold)
    // Add additional font styles as needed
)


data class TodoItem(
    val title: String,
    val dueDate: String?,
    val category: String,
    val completed: Boolean = false
)

fun getDaysBetweenDates(date1: String, date2: String): Long {
    val startDate = LocalDate.parse(date1)
    val endDate = LocalDate.parse(date2)
    return ChronoUnit.DAYS.between(startDate, endDate)
}