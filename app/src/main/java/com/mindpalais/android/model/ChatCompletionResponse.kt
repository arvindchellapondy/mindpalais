package com.mindpalais.android.model

import com.google.gson.annotations.SerializedName

data class ChatCompletionResponse(
    val id: String,
    @SerializedName("object") val obj: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

data class Choice(
    val message: Message,
    val finish_reason: String,
    val index: Int
)

data class Message(
    val role: String,
    val content: String
)