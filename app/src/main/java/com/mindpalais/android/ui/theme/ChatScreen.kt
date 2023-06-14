package com.mindpalais.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.mindpalais.android.R
import com.mindpalais.android.model.Message
import com.mindpalais.android.model.MindMapViewModel
import com.mindpalais.android.model.ResponseModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ChatScreen(
    mindMapViewModel: MindMapViewModel,
    onSendMessage: (message: String) -> Unit
) {

    val chatMessages = remember { mutableStateOf(emptyList<Message>()) }
    val messagesListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        mindMapViewModel.chatMessages.collect { messages ->
            chatMessages.value = messages
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),

        ) {

        MessagesList(
            messages = chatMessages.value,
            modifier = Modifier.weight(1f),

        )
        Divider()
        SendMessage(
            mindMapViewModel,
            onSendMessage = onSendMessage,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun MessagesList(
    messages: List<Message>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(messages) { message ->
            if (message.role.equals("assistant")) {
                SentMessage(message = message.content)
            } else if(message.role.equals("user")) {
                ReceivedMessage(message = message.content)
            }
        }
    }
}

@Composable
fun ReceivedMessage(message: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 100.dp, end = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(id = R.color.light_primary_bkg), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column {

                Text(
                    text = message,
                    color = Color.DarkGray,
                    fontFamily = interFontFamily,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp
                )
            }

        }
    }
}

@Composable
fun SentMessage(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 100.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    darkPrimary,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Column() {

                TypewriterText(text = message)

//                Text(
//                    text = message,
//                    color = Color.White,
//                    fontFamily = interFontFamily,
//                    fontWeight = FontWeight.Normal,
//                    lineHeight = 24.sp
//                )
            }

        }
    }
}

@Composable
fun SendMessage(
    viewModel: MindMapViewModel,
    onSendMessage: (message: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    val context = LocalContext.current
    Row(
        modifier = modifier
            .padding(start = 0.dp, end = 0.dp, bottom = 16.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Message", fontFamily = interFontFamily, fontWeight = FontWeight.Normal) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText)
                        messageText = ""
                    }
                }
            )
        )
        IconButton(
            onClick = {
                if (messageText.isNotBlank()) {
                    //onSendMessage(messageText)
                    //viewModel.sendMessageToAssistant(messageText)
                    //messageText = ""
                }
            }
        ) {
            Icon(Icons.Filled.Send, "Send")
        }
    }
}

fun getCall(userMessage: String?) {

    runBlocking {
        val apiKey = "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"
        val url = "https://api.openai.com/v1/chat/completions"
        val mediaType = "application/json".toMediaType()

        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val content =
            "date of this message : $currentDate, convert this to json format - include message, action, date in proper format and the person involved : $userMessage"
        val requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [{"role": "user", "content": "$content"}]
            }
            """.trimIndent().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        //println("HTTP request: ${request.method} ${request.url}")

        println(request.body)


        val response = withContext(Dispatchers.IO) {
            OkHttpClient().newCall(request).execute()
        }
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            // Parse the response JSON into the response model
            val gson = Gson()
            val responseModel = gson.fromJson(responseBody, ResponseModel::class.java)

        } else {
            println("HTTP request failed with code ${response.code}")
        }
    }
}

data class ChatMessage(
    val id: Int,
    val senderName: String,
    val text: String,
//    val timestamp: Long
)



