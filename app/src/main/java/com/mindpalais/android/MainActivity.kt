package com.mindpalais.android

import DatabaseHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image;
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.navigation.compose.*
//import com.example.myapp.ui.chat.ChatScreen
//import com.example.myapp.ui.todo.TodoScreen
//import com.example.myapp.ui.calendar.CalendarScreen
//import com.example.myapp.ui.contacts.ContactsScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.ui.theme.*

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mindpalais.android.model.UiViewModel


class MainActivity : ComponentActivity() {

    //    private val viewModel by lazy {
//        ViewModelProvider(applicationContext).get(MindMapViewModel::class.java)
//    }
    private val mindMapViewModel by viewModels<MindMapViewModel>()
    private val uiViewModel by viewModels<UiViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DatabaseHelper(this)


        setContent {
            var isLoading by remember { mutableStateOf(true) }
            MindpalaisTheme(
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
//                    Greeting("Android")


                }
            }

            if (isLoading) {
                SplashScreen(onSplashClick = {
                    isLoading = false
                })
            } else {
                MindpalaisApp(mindMapViewModel, uiViewModel)
            }

        }
    }

    override fun onPostResume() {
        super.onPostResume()
        mindMapViewModel.fetchResponses()
    }

    override fun onResume() {
        super.onResume()
        mindMapViewModel.fetchResponses()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MindpalaisApp(mindMapViewModel: MindMapViewModel, uiViewModel: UiViewModel) {
    val navController = rememberNavController()

    @OptIn(ExperimentalMaterialApi::class)
//    val bottomSheetContent = remember { mutableStateOf<@Composable ColumnScope.() -> Unit>({}) }
    val bottomSheetHeight = remember { mutableStateOf(60.dp) }
    val bottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

    val bottomSheetContent = uiViewModel.bottomSheetContent.value
    val expandBottomSheet by uiViewModel.expandBottomSheet.collectAsState()
    val isBottomSheetSwipeable by uiViewModel.isBottomSheetSwipeable.collectAsState()


    val context = LocalContext.current
    //val viewModel: MindMapViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = Color(0xFF000000), // Set the desired status bar color
//        darkIcons = useDarkIcons
    )


    var showDialog by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    val focusRequester = FocusRequester.Default

//    LaunchedEffect(bottomSheetState.isVisible) {
//        if (bottomSheetState.isVisible) {
//            Log.e("line 141", "MpApp bottomSheetState.isVisible - visible")
//        } else {
//            Log.e("line 143", "MpApp bottomSheetState.isVisible - hidden")
//
//        }
//    }

    LaunchedEffect(expandBottomSheet) {
        // bottomSheetState.show()
        Log.e(
            "line 148",
            "MpApp bottomSheet - CurrentValue :" + bottomSheetState.currentValue
        )
        Log.e("line 151", "MpApp expandBottomSheet " + expandBottomSheet)
        if (expandBottomSheet && bottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
            Log.e("line 154", "MpApp expandBottomSheet - visible")
            //uiViewModel.setSwipeable(false)
            bottomSheetState.show()
        } else {
            Log.e("line 154", "MpApp expandBottomSheet - hidden")
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetState.currentValue }
            .collect {
                Log.e(
                    "line 161",
                    "MpApp bottomSheet - CurrentValue :" + bottomSheetState.currentValue
                )

                if (bottomSheetState.currentValue == ModalBottomSheetValue.Expanded) {

                } else if (bottomSheetState.currentValue == ModalBottomSheetValue.Hidden) {
                    uiViewModel.setExpandBottomSheet(false)
                } else if (bottomSheetState.currentValue == ModalBottomSheetValue.HalfExpanded) {
                    bottomSheetState.targetValue
                }
            }
    }


    if (showDialog) {

        if (currentRoute == "todo" || currentRoute == "calendar") {
            Dialog(onDismissRequest = { showDialog = false }, content = {
                Card(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(360.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        TextField(
                            value = textValue,
                            onValueChange = { textValue = it },
                            placeholder = { Text("What would you like to schedule? ") },
                            modifier = Modifier
                                .background(Color.Transparent)
                                .height(300.dp)
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Row() {
                            TextButton(
                                onClick = {
                                    // Do something with the entered text value here
                                    showDialog = false
                                }, colors = ButtonDefaults.textButtonColors(
                                    backgroundColor = Color.Transparent,
                                    contentColor = Color.Red // Change the text color to red
                                ), modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(Icons.Filled.Cancel, contentDescription = "Cancel")
                                //Text("Cancel")
                            }
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    if (textValue.isNotBlank()) {
                                        mindMapViewModel.sendMessageToAssistant(textValue)
                                    }
                                    Toast.makeText(
                                        context, "Adding item...", Toast.LENGTH_SHORT
                                    ).show()
                                    textValue = ""
                                }, colors = ButtonDefaults.buttonColors(
                                    backgroundColor = lightBlue, contentColor = Color.White
                                ), modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add")
//                            Text("Add")
                            }

                            MicTextButton(
                                onClick = { /* do something */ }, text = ""
                            )
                        }

                    }
                }
            })
        } else if (currentRoute == "notes") {
            uiViewModel.bottomSheetContent.value = { AddNoteScreen(mindMapViewModel,uiViewModel) }
            uiViewModel.setExpandBottomSheet(true)

            showDialog = false
        }
    }

    val sheetContent: @Composable ColumnScope.() -> Unit = {
        // Content of the bottom sheet
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //bottomSheetContent
        }
    }


    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { showDialog = true },
            backgroundColor = darkPrimary,
            contentColor = Color.White
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    },

        bottomBar = {
            Column() {

                Divider(
                    color = lightSecondary,
                    thickness = 2.dp, startIndent = 0.dp,
                )

                BottomNavigation(
                    backgroundColor = Color(0xFF84A09B),
                ) {


                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "To-Do"
                        )
                    },
                        selected = currentRoute == "todo",
                        selectedContentColor = Color.White,
                        unselectedContentColor = lightSecondary,
                        onClick = {
                            navController.navigate("todo") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                launchSingleTop = true
//                                restoreState = true
                            }
                        })

                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.CalendarToday, contentDescription = "Calendar"
                        )
                    },
                        selected = currentRoute == "calendar",
                        selectedContentColor = Color.White,
                        unselectedContentColor = lightSecondary,
                        onClick = {
                            navController.navigate("calendar") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                // launchSingleTop = true
//                                restoreState = false
                            }
                        })

                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.People,
                            contentDescription = "Contacts"
                        )
                    },
                        selected = currentRoute == "contacts",
                        selectedContentColor = Color.White,
                        unselectedContentColor = lightSecondary,
                        onClick = {
                            navController.navigate("contacts") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                // launchSingleTop = true
                                // restoreState = true
                            }
                        })

                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.StickyNote2,
                            contentDescription = "Notes"
                        )
                    },
                        selected = currentRoute == "notes",
                        selectedContentColor = Color.White,
                        unselectedContentColor = lightSecondary,
                        onClick = {
                            navController.navigate("notes") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                //launchSingleTop = true
                                // restoreState = true
                            }
                        })

                    BottomNavigationItem(icon = {
                        Icon(
                            Icons.Filled.FileCopy,
                            contentDescription = "Files"
                        )
                    },
                        selected = currentRoute == "files",
                        selectedContentColor = Color.White,
                        unselectedContentColor = lightSecondary,
                        onClick = {
                            navController.navigate("files") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                //launchSingleTop = true
                                // restoreState = true
                            }
                        })

                }
            }
        }) {

        Box(
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.stickler_bkg1),
                contentDescription = "background_image",
                contentScale = ContentScale.FillBounds,
//                colorFilter = ColorFilter.tint(Color.White.copy(alpha =  0.2f))

            )
        }

        NavHost(
            navController = navController, startDestination = "todo"
        ) {
//            composable("chat") { HomeScreen(viewModel) }
            composable("todo") { TodoScreen(mindMapViewModel, uiViewModel) }
            composable("calendar") {
                CalendarScreen(mindMapViewModel, uiViewModel)
            }
            composable("contacts") { ContactsScreen(mindMapViewModel) }
            composable("notes") { NotesScreen(mindMapViewModel, uiViewModel) }
            composable("files") { FilesScreen(mindMapViewModel,uiViewModel) }
        }
    }

//        val showBottomSheet = remember { mutableStateOf(true) }

//        if (showBottomSheet.value) {
//            BottomSheet(
//                sheetContent = bottomSheetContent.value,
//                sheetState = bottomSheetState,
//                sheetHeight = bottomSheetHeight.value,
//                onDismiss = {
//                    showBottomSheet.value = false
//                }
//            )
//        }

    // ModalBottomSheetLayout to show the bottom sheet
    ModalBottomSheetLayout(
        sheetContent = {
            // Content of the bottom sheet
            Surface(
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.stickler_bkg1),
                        contentDescription = "background_image",
                        contentScale = ContentScale.FillBounds
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(whiteSmoke.copy(alpha = 0.6f)),
                        verticalArrangement = Arrangement.Top
                    ) {


                        Column() {
                            Divider(
                                color = whiteGray,
                                thickness = 4.dp, startIndent = 0.dp,
                                modifier = Modifier.padding(180.dp, 6.dp, 180.dp, 6.dp)
                            )




                            bottomSheetContent.invoke()
                        }


                    }


                }


            }

        }, sheetState = bottomSheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 16.dp
    ) {
        // Rest of the screen content
    }


//    @Composable
//    fun observeBottomSheetState(bottomSheetState: ModalBottomSheetState) {
//        DisposableEffect(bottomSheetState.isVisible) {
//            val bottomSheetStateObserver = bottomSheetState.isVisible.{ isVisible ->
//                if (isExpanded) {
//                    // The bottom sheet is expanded
//                    // Perform actions or update state accordingly
//                } else {
//                    // The bottom sheet is collapsed
//                    // Perform actions or update state accordingly
//                }
//            }
//
//            onDispose {
//                bottomSheetStateObserver.dispose()
//            }
//        }
//    }


    @Composable
    fun Rectangle() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xff543695), Color(0xffe6a6f1)),
                        startY = 180f,
                        endY = 500f
                    )
                )
        )
    }

//@Composable
//fun ChatScreen() {
//    Surface(color = androidx.compose.ui.graphics.Color.White) {
//        Text("Chat Screen")
//    }
//}

//@Composable
//fun TodoScreen() {
//    Surface(color = androidx.compose.ui.graphics.Color.White) {
//        Text("To-Do Screen")
//    }
//}

//@Composable
//fun CalendarScreen() {
//    Surface(color = androidx.compose.ui.graphics.Color.White) {
//        Text("Calendar Screen")
//    }
//}

//    @SuppressLint("CoroutineCreationDuringComposition")
//    @OptIn(ExperimentalMaterialApi::class)
//    @Composable
//    fun collapseBottomSheet(bottomSheetState: BottomSheetScaffoldState) {
//        val scope = bottomSheetState.bottomSheetState::class.java.getDeclaredField("scope")
//            .apply { isAccessible = true }
//            .get(bottomSheetState.bottomSheetState) as CoroutineScope
//
//        scope.launch { bottomSheetState.bottomSheetState.collapse() }
//    }

//    @Composable
//    fun Greeting(name: String) {
//        Text(text = "Hello $name!")
//    }

    @Composable
    fun DefaultPreview() {
        MindpalaisTheme {
            // Greeting("Android")
        }
    }

    fun addTodoItem(mesage: String) {

    }

    @Composable
    fun OverlayView(onSubmit: (String) -> Unit, onMicClick: () -> Unit) {
        var text by remember { mutableStateOf("") }

        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Card(
//            elevation = 8.dp,
                backgroundColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Enter text...") },
                        label = { Text("Message") },
                        maxLines = 4,
                        textStyle = MaterialTheme.typography.body1.copy(fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onMicClick() }) {
                            Icon(Icons.Filled.Mic, contentDescription = "Speak")
                            Spacer(Modifier.width(8.dp))
                            Text("Speak")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { onSubmit(text) }) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}