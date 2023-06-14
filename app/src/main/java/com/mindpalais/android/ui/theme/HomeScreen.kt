package com.mindpalais.android.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindpalais.android.R
import com.mindpalais.android.model.MindMapViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(viewModel: MindMapViewModel) {
    val items = remember { mutableStateListOf<TodoItem>() }
    val (selectedTab, setSelectedTab) = remember { mutableStateOf(0) }

    val scaffoldState = rememberScaffoldState()

    var isRecipientSelected by remember { mutableStateOf(false) }

    val recipients = listOf<String>("Watson", "Arteta", "Sydney")

    val chatMessages = listOf(
        ChatMessage(0, "Watson","Hi there! I'm Watson, your AI assitant. What would you like to add to your plans?"),
        ChatMessage(1, "Kesan","Plan Vegas trip for Kumaran's bachelors.",),
        ChatMessage(2, "Watson", "Sure, Adding that right away.",),
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MindpalaisTopAppBar({ getHomeTopAppBar() })
        },
        content = {
            Column {
//                if (!isRecipientSelected) {
//                    ChatRecipients(recipients, scaffoldState, {
//                        isRecipientSelected = true
//                    })
//                } else {
//
//                }

                ChatScreen(mindMapViewModel = viewModel) {}

            }
        }
    )


    // Scroll to a specific page when selectedTab changes
//    LaunchedEffect(selectedTab) {
//        pagerState.scrollToPage(selectedTab)
//    }
}

@Composable
fun ChatRecipients(items: List<String>, scaffoldState: ScaffoldState,onItemClick: () -> Unit) {
    LazyColumn {
        items(items) { item ->
            ChatRecipient(item = item, onItemClick = onItemClick)
            Divider(
                color = lightPrimary,
                thickness = 1.dp, startIndent = 12.dp,
                modifier = Modifier.padding(vertical = 0.dp, horizontal = 0.dp)
            )
        }
    }
}

@Composable
fun ChatRecipient(item: String, onItemClick: () -> Unit) {

    Row(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            item, fontFamily = interFontFamily, fontWeight = FontWeight.Normal,
            color = Color.DarkGray, lineHeight = 48.sp, fontSize = 16.sp
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = if (item.equals("Watson") || item.equals("Sydney")) darkBlue else darkGreen
        )

    }
}

@Composable
fun getHomeTopAppBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mp),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.LightGray
            )
        }

        Text(
            text = "mindpalais", fontFamily = interFontFamily, fontWeight = FontWeight.Medium,
            fontSize = 24.sp, color = Color.LightGray
        )

    }
}
