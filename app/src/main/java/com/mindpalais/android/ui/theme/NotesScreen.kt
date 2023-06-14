package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalais.android.model.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(mindMapViewModel: MindMapViewModel, uiViewModel: UiViewModel) {
    val notes = mindMapViewModel.notes.collectAsState(initial = emptyList()).value
    val expandBottomSheet by uiViewModel.expandBottomSheet.collectAsState()

    val messages = mutableListOf<Message>()

    val gridState = rememberLazyGridState()
    var selectedItem by remember {
        mutableStateOf(
            Note(
                "",
                "",
                "",
                "",
                arrayOf(),
                arrayOf(),
            )
        )
    }
    LaunchedEffect(Unit) {
        mindMapViewModel.fetchNotes()
        gridState.scrollToItem(0)
    }

    LaunchedEffect(expandBottomSheet) {
        if (expandBottomSheet) {
        } else {
            mindMapViewModel.clearChatMessages()
        }
    }

    val onSendMessage: (message: String) -> Unit = { message ->
        messages.add(Message(role = "user", content = message))
        mindMapViewModel.setChatMessages(Message(role = "user", content = message))
        val updatedMessages = mutableListOf<Message>()
        updatedMessages.add(messages.get(0))
        updatedMessages.addAll(if (messages.size > 3) messages.takeLast(3) else messages)
        mindMapViewModel.askAssistant(updatedMessages)
    }

    val onItemClick: (Note) -> Unit = { sItem ->
        val content =  sItem.note

        messages.clear()
        messages.add(
            Message(
                role = "system",
                content = "You are a NoteGPT. consider the following note : "+ sItem.note
            )
        )

        mindMapViewModel.askAssistant(messages)

        uiViewModel.setBottomSheetContent {
            getNoteBottomSheetContent(mindMapViewModel, onSendMessage)
            uiViewModel.setExpandBottomSheet(true)
        }

    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
            MindpalaisTopAppBar({ getNotesTopBar() })
        },
        content = {
            LazyVerticalGrid(
                GridCells.Fixed(2),
                state = gridState
            ) {
                items(notes.reversed()) { note ->
                    NoteItem(note = note, onItemClick)
                }
            }
        }
    )
}

@Composable
fun NoteItem(note: Note, onItemClick: (Note) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(180.dp)
            .clickable(onClick = {
                onItemClick(note)
            }),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = note.note,
                color = darkPrimary,
                fontFamily = interFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.weight(1f),
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun getNotesTopBar() {
    Text(
        text = "Notes", fontFamily = interFontFamily, color = Color.White,
        fontWeight = FontWeight.Bold, fontSize = 18.sp
    )
}

@Composable
fun getNoteBottomSheetContent(
    mindMapViewModel: MindMapViewModel,
    onSendMessage: (message: String) -> Unit
) {
    return ChatScreen(
        mindMapViewModel = mindMapViewModel,
        onSendMessage = onSendMessage
    )
}


