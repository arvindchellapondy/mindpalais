package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.model.ResponseModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ContactsScreen(viewModel: MindMapViewModel) {
//    val recipients = listOf<String>("Kumaran", "Malavika", "Anvee")
    val responses = viewModel.responses.collectAsState(initial = emptyList()).value
    val peopleList = responses.flatMap { it.people.toList() }.distinct().filter { it.isNotEmpty() }

    val personToItemsMap = mutableMapOf<String, MutableList<String>>()
    for (person in peopleList) {
        personToItemsMap[person] = mutableListOf()
    }
    for (item in responses) {
        for (person in item.people) {
            if (person.isNotEmpty()) {
                personToItemsMap[person]?.add(item.message)
            }
        }
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
            MindpalaisTopAppBar({ getContactsTopAppBar() })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //ChatRecipients(peopleList, scaffoldState, {})
                TodoList(todoItems = personToItemsMap)
            }
        }
    )
}

@Composable
fun getContactsTopAppBar() {
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
                text = "People", fontFamily = interFontFamily,
                fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White
            )
        }
    }
}

@Composable
fun TodoList(todoItems: Map<String, List<String>>) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        todoItems.forEach { (person, todos) ->
            Card(
                Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {

                    Column() {
                        Box(
                            modifier = Modifier
                                .background(Color.Transparent)
                                .fillMaxWidth()
                        ) {
                            Text(
                                person, fontFamily = interFontFamily, fontWeight = FontWeight.Bold,
                                color = darkPrimary, lineHeight = 24.sp, fontSize = 16.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }


                    Divider(
                        color = whiteGray,
                        thickness = 1.dp, startIndent = 0.dp
                    )
                    todos.forEachIndexed { index, todo ->
                        val isLast = index == todos.lastIndex
                        //Text(todo, modifier = Modifier.padding(top = 8.dp))
                        ContactTodoItem(item = todo)
                        if (!isLast) {
                            Divider(
                                color = whiteGray,
                                thickness = 1.dp, startIndent = 12.dp
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ContactTodoItem(item: String) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                item, fontFamily = interFontFamily, fontWeight = FontWeight.Normal,
                color = Color.DarkGray, lineHeight = 24.sp, fontSize = 16.sp
            )
//            Text(item.dueDate!!)
        }
//        Checkbox(checked = item.completed, onCheckedChange = {})
    }
}
