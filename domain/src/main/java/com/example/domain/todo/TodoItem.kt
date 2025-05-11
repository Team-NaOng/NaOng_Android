package com.example.domain.todo

data class TodoItem(
    val title: String,
    val hasRepeat: Boolean = false,
    var isDone: Boolean = false,
    val hasTime: Boolean = true,
    val category: String = "위치"
)
