package com.example.domain.todo.model

enum class RepeatType(val value: String) {
    ALL("전체"),
    ONCE("한 번"),
    REPEAT("반복");

    companion object {
        fun fromString(value: String): RepeatType {
            return values().find { it.value == value } ?: ALL
        }
    }
}
