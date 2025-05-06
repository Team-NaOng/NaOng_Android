package com.example.presentation.data

data class TodoItem(
    val title: String,
    val hasRepeat: Boolean = false,
    var isDone: Boolean = false,
    val hasTime: Boolean = true,
)
