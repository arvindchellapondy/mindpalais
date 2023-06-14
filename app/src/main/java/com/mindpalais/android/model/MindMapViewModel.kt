package com.mindpalais.android.model

import DatabaseHelper
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class MindMapViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application.applicationContext)

    private val _responses = MutableStateFlow<List<ResponseModel>>(emptyList())
    val responses: StateFlow<List<ResponseModel>> = _responses

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _chatMessages = MutableStateFlow<List<Message>>(emptyList())
    val chatMessages: StateFlow<List<Message>> = _chatMessages

    fun sendMessageToAssistant(message: String) {

//        gpt-3.5-turbo
        viewModelScope.launch {
            val apiKey = "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"
            val url = "https://api.openai.com/v1/chat/completions"
            val mediaType = "application/json".toMediaType()
            val name = "Arvind"
            val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val content =
                "date of this message : $currentDate, message : $message, " +
                        "Please convert this todo item to a json formatted model with  the " +
                        "message : message,"+
                        "action :  action to be taken specific not generalized, " +
                        "date : exact day/date of action converted to date format in yyyy-mm-dd and if its a recurring event give list of dates for next 6 months" +
                        "the people: list of people involved in the message, $name if there is no one involved."
            val requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [{"role": "user", "name":"$name", "content": "$content"}]
            }
            """.trimIndent().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $apiKey")
                .post(requestBody)
                .build()

            //println("HTTP request: ${request.method} ${request.url}")

            println(request.body)

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
                .readTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.e("response:", responseBody.toString())
                // Parse the response JSON into the response model
                val gson = Gson()
                val chatCompletionResponse = gson.fromJson(responseBody, ChatCompletionResponse::class.java)

                val responseModel = gson.fromJson(chatCompletionResponse.choices.get(0).message.content, ResponseModel::class.java)
                responseModel.created = chatCompletionResponse.created.toString()
                responseModel.status = TaskStatus.READY

                val isInsertSuccess = dbHelper.insertResponseToDatabase(responseModel)
                Toast.makeText(getApplication(), "Item added", Toast.LENGTH_SHORT).show()

                fetchResponses()


            } else {
                Toast.makeText(getApplication(), "Item add - failed", Toast.LENGTH_SHORT).show()
                println("HTTP request failed with code ${response.code}")
            }
        }
    }

    fun sendNoteToAssistant(note: String) {
        viewModelScope.launch {
            val apiKey = "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"
            val url = "https://api.openai.com/v1/chat/completions"
            val mediaType = "application/json".toMediaType()
            val name = "Arvind"
            val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val content =
                "$note"
            val requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                {"role": "system", "content": "You are taking notes of a meeting."},
                {"role": "user", "name":"$name", "content": "$note"}]
            }
            """.trimIndent().toRequestBody(mediaType)
//            val content =
//                "summarize the note : $note "
//            +
//                        "Summarize the note and return in a json format " +
//                        "note : contains the original note text,"+
//                        "summary :  provides a summarized version of the note," +
//                        "word_cloud: a list of words extracted from the $note"
//            val requestBody = """
//            {
//                "model": "gpt-3.5-turbo",
//                "messages": [{"role": "user", "name":"$name", "content": "$content"}]
//            }
//            """.trimIndent().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            println(request.body)


            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
                .readTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

//            val response = withContext(Dispatchers.IO) {
//                withTimeout(60_000L) { // Set a timeout duration of 30 seconds (30,000 milliseconds)
//                    OkHttpClient().newCall(request).execute()
//                }
//            }
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.e("response:", responseBody.toString())
                // Parse the response JSON into the response model
//                val gson = Gson()
//                val chatCompletionResponse = gson.fromJson(responseBody, ChatCompletionResponse::class.java)
//
//                val responseModel = gson.fromJson(chatCompletionResponse.choices.get(0).message.content, ResponseModel::class.java)
//                responseModel.created = chatCompletionResponse.created.toString()
//                responseModel.status = TaskStatus.READY
//
//                val isInsertSuccess = dbHelper.insertResponseToDatabase(responseModel)
//                Toast.makeText(getApplication(), "Item added", Toast.LENGTH_SHORT).show()
//
//                fetchResponses()


            } else {
                Toast.makeText(getApplication(), "Item add - failed", Toast.LENGTH_SHORT).show()
                Log.e("HTTP request failed with code", response.code.toString())
            }
        }
    }

    fun askAssistant(messages: List<Message>) {
        viewModelScope.launch {
            val apiKey = "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"
            val url = "https://api.openai.com/v1/chat/completions"
            val mediaType = "application/json".toMediaType()
            val name = "Arvind"
            val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

            val messagesJson = buildMessagesJson(messages)

            val requestBody = """
            {
                "model": "gpt-3.5-turbo",
                "messages": $messagesJson
            }
        """.trimIndent().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            println(request.body)


            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
                .readTimeout(30, TimeUnit.SECONDS) // Increase the timeout duration
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

//            val response = withContext(Dispatchers.IO) {
//                withTimeout(60_000L) { // Set a timeout duration of 30 seconds (30,000 milliseconds)
//                    OkHttpClient().newCall(request).execute()
//                }
//            }
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.e("response:", responseBody.toString())
                // Parse the response JSON into the response model
                val gson = Gson()
                val chatCompletionResponse = gson.fromJson(responseBody, ChatCompletionResponse::class.java)

                setChatMessages(chatCompletionResponse.choices.get(0).message)
//                val responseModel = gson.fromJson(chatCompletionResponse.choices.get(0).message.content, ResponseModel::class.java)
//                responseModel.created = chatCompletionResponse.created.toString()
//                responseModel.status = TaskStatus.READY
//
//                val isInsertSuccess = dbHelper.insertResponseToDatabase(responseModel)
//                Toast.makeText(getApplication(), "Item added", Toast.LENGTH_SHORT).show()
//
//                fetchResponses()


            } else {
                Toast.makeText(getApplication(), "Item add - failed", Toast.LENGTH_SHORT).show()
                Log.e("HTTP request failed with code", response.code.toString())
            }
        }
    }

    fun updateResponse(response: ResponseModel): Boolean {
        val rowsAffected = dbHelper.updateResponseInDatabase(response)
        if (rowsAffected) {
            fetchResponses() // Update the responses flow with the latest data
            return true
        }
        return false
    }



    fun fetchResponses() {
        viewModelScope.launch {
            _responses.value = dbHelper.getResponses()
        }
    }

    fun deleteResponse(response: ResponseModel): Boolean {
        val rowsAffected = dbHelper.deleteResponse(response.id)
        if (rowsAffected) {
            fetchResponses() // Update the responses flow with the latest data
            return true
        }
        return false
    }

    // Function to insert a new note into the database
    fun insertNote(note: Note) {
        // Perform the insert operation in a coroutine scope
        viewModelScope.launch {
            // Insert the note into the database
            val success = dbHelper.insertNoteToDatabase(note)

            // Check if the insert operation was successful
            if (success) {
                Log.e("line 216", "mindMaPViewMOdel - success")
                // Get the updated list of notes from the database
                val updatedNotes = dbHelper.getNotes()

                // Update the ViewModel's notes property with the new data
                _notes.value = updatedNotes
            }
        }
    }

    fun fetchNotes(){
        viewModelScope.launch {
            _notes.value = dbHelper.getNotes()
        }
    }

    private fun buildMessagesJson(messages: List<Message>): String {
        val jsonBuilder = StringBuilder()
        jsonBuilder.append("[")
        messages.forEachIndexed { index, message ->
            jsonBuilder.append("{")
            jsonBuilder.append("\"role\": \"${message.role}\",")
            jsonBuilder.append("\"content\": \"${message.content}\"")
            jsonBuilder.append("}")
            if (index < messages.size - 1) {
                jsonBuilder.append(",")
            }
        }
        jsonBuilder.append("]")
        return jsonBuilder.toString()
    }

    fun setChatMessages(message: Message) {
        val currentMessages = _chatMessages.value.toMutableList()
        currentMessages.add(message)
        _chatMessages.value = currentMessages
    }

    fun clearChatMessages(){
        _chatMessages.value = emptyList()
    }
}