package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.model.ResponseModel
import com.mindpalais.android.model.UiViewModel
import com.mindpalais.android.ui.theme.calendar.Week
import com.mindpalais.android.ui.theme.calendar.WeekCalendarImpl
import com.mindpalais.android.ui.theme.calendar.WeekCalendarState
import com.mindpalais.android.ui.theme.calendar.WeekDay
import kotlinx.coroutines.flow.filter
import java.time.DayOfWeek


import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun CalendarScreen(
    mindMapViewModel: MindMapViewModel,
    uiViewModel: UiViewModel
) {

    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }

    val items by mindMapViewModel.responses.collectAsState(emptyList())

    val scaffoldState = rememberScaffoldState()

    val bottomSheetContent = uiViewModel.bottomSheetContent.value

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)

    var filteredItems = remember { mutableStateListOf<ResponseModel>() }

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
                "",
//                arrayOf()
            )
        )
    }
    val expandBottomSheet by uiViewModel.expandBottomSheet.collectAsState()

    LaunchedEffect(expandBottomSheet) {
        if(expandBottomSheet){
            Log.e("line 83","CalScreen expandBottomSheet - visible")

        }else{
            Log.e("line 85","CalScreen expandBottomSheet - hidden")
            //showDialog = false

        }
    }

    val onItemClick: (ResponseModel) -> Unit = { sItem ->
        selectedItem = sItem
        showDialog = true

        Log.e("line 96","calScreen selectedItem "+ selectedItem.action)
        uiViewModel.setBottomSheetContent {
            getBottomSheetContent(selectedItem,showDialog)
            uiViewModel.setExpandBottomSheet(true)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
            MindpalaisTopAppBar({ GetCalendarTopAppBar(visibleWeek) })
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
            ) {
                WeekCalendar(
                    modifier = Modifier.background(color = Color(0x88FFFFFF)),
                    state = state,
                    dayContent = { day ->
                        Day(day.date, isSelected = selection == day.date) { clicked ->
                            if (selection != clicked) {
                                selection = clicked
                                filteredItems.clear()
                                filteredItems.addAll(items.filter { it.date.equals(day.date.toString()) })
                            }
                        }
                    },
                )

                Divider(
                    color = whiteGray,
                    thickness = 1.dp, startIndent = 0.dp,
                )
                val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val filteredItems = remember(items, selection) {
                    mutableStateListOf<ResponseModel>().apply {
                        addAll(items.filter { it.date.equals(selection.toString()) })
                    }
                }
//                filteredItems.clear()
//                filteredItems.addAll(items.filter { it.date.equals(currentDate.toString()) })
                //TodoList(items = filteredItems ?: emptyList())

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

                            //  CalendarScreen(viewModel = viewModel)
                        }
                    }
                }

                TodoList(items = filteredItems, onItemClick)
                if (selectedItem != null && showDialog) {

//                    uiViewModel.setBottomSheetContent {
//                        getBottomSheetContent(selectedItem,showDialog)
//                        uiViewModel.setExpandBottomSheet(true)
//                    }


                }
            }
        })
}

@Composable
fun getBottomSheetContent(selectedItem: ResponseModel, showDialog: Boolean) {
    return CustomDialog(
        response = selectedItem,
        onSaveClick = { updatedResponse ->
            //mindMapViewModel.updateResponse(updatedResponse)
           // showDialog = false
        },
        onDeleteClick = {
            //mindMapViewModel.deleteResponse(selectedItem)
           // showDialog = false
        },
        onCancelClick = {
        //    showDialog = false
        }
    )
}

@Composable
fun rememberWeekCalendarState(
    startDate: LocalDate = YearMonth.now().atStartOfMonth(),
    endDate: LocalDate = YearMonth.now().atEndOfMonth(),
    firstVisibleWeekDate: LocalDate = LocalDate.now(),
    firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale(),
): WeekCalendarState {
    return rememberSaveable(saver = WeekCalendarState.Saver) {
        WeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = firstVisibleWeekDate,
            firstDayOfWeek = firstDayOfWeek,
            visibleItemState = null,
        )
    }
}

@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }
        firstDate.year == lastDate.year -> {
            "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
        }
        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}

@Composable
fun NavigationIcon(onBackClick: () -> Unit) {
//    Box(
//        modifier = Modifier
//            .fillMaxHeight()
//            .aspectRatio(1f)
//            .padding(8.dp)
//            .clip(shape = CircleShape)
//            .clickable(role = Role.Button, onClick = onBackClick),
//    ) {
//        Icon(
//            tint = Color.White,
//            modifier = Modifier.align(Alignment.Center),
//            imageVector = Icons.Default.ArrowBack,
//            contentDescription = "Back",
//        )
//    }
}

@Composable
fun WeekCalendar(
    modifier: Modifier = Modifier,
    state: WeekCalendarState = rememberWeekCalendarState(),
    calendarScrollPaged: Boolean = true,
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    dayContent: @Composable BoxScope.(WeekDay) -> Unit,
    weekHeader: (@Composable ColumnScope.(Week) -> Unit)? = null,
    weekFooter: (@Composable ColumnScope.(Week) -> Unit)? = null,
) = WeekCalendarImpl(
    modifier = modifier,
    state = state,
    calendarScrollPaged = calendarScrollPaged,
    userScrollEnabled = userScrollEnabled,
    reverseLayout = reverseLayout,
    dayContent = dayContent,
    weekHeader = weekHeader,
    weekFooter = weekFooter,
    contentPadding = contentPadding,
)

@Composable
private fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {

    val backgroundColor = if (isSelected) darkPrimary2 else Color.Transparent
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(backgroundColor)
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.dayOfWeek.displayText(),
                fontSize = 16.sp,
                color = if (isSelected) Color.White else darkPrimary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
            Text(
                text = dateFormatter.format(date),
                fontSize = 16.sp,
                color = if (isSelected) Color.White else darkPrimary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(darkPrimary)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
fun GetCalendarTopAppBar(visibleWeek: Week) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TopAppBar(
//            title = {
//                Text(
//                    text = getWeekPageTitle(visibleWeek),
//                    fontFamily = interFontFamily,
//                    fontWeight = FontWeight.Normal,
//                    fontSize = 24.sp,
//                    color = Color.LightGray
//                )
//            }, backgroundColor = darkPrimary,
//        )
//    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp)
        ) {
            Text(
                text = getWeekPageTitle(visibleWeek), fontFamily = interFontFamily,
                fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White
            )

        }


    }
}

fun YearMonth.atStartOfMonth(): LocalDate = this.atDay(1)

fun firstDayOfWeekFromLocale(): DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(year, month)

val YearMonth.nextMonth: YearMonth
    get() = this.plusMonths(1)

val YearMonth.previousMonth: YearMonth
    get() = this.minusMonths(1)

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

