package com.mindpalais.android.model

data class Note(
    val id: String,
    val createdAt: String,
    val note: String,
    val title: String,
    val generated: Array<String>,
    val wordcloud: Array<String>
)