package com.example.domain.todo.model

enum class TodoCategory(val value: String) {
    TIME("시간"),
    LOCATION("위치");

    companion object {
        fun fromString(value: String): TodoCategory {
            return values().find { it.value == value } ?: LOCATION
        }
    }
}
