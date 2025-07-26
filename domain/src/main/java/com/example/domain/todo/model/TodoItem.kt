package com.example.domain.todo.model

data class TodoItem(
    val title: String,
    val hasRepeat: Boolean,
    var isDone: Boolean,
    val isToday: Boolean,
    val category: TodoCategory, // 여기 바뀜
    val time: String? = null
)