package com.example.domain.todo.model

/**
 * 할 일 항목을 나타내는 도메인 모델
 *
 * @property title 할 일 제목
 * @property hasRepeat 반복 여부
 * @property isDone 완료 여부
 * @property isToday 오늘 할 일 여부
 * @property category 할 일 카테고리
 * @property time 할 일 시간 (선택사항)
 */

data class TodoItem(
    val title: String,
    val hasRepeat: Boolean,
    var isDone: Boolean,
    val isToday: Boolean,
    val category: TodoCategory,
    val time: String? = null
)