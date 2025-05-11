package com.example.data.todo

import com.example.domain.todo.GetFilteredTodoUseCase
import com.example.domain.todo.TodoItem
import javax.inject.Inject

class ImplementUseCaseGetFilteredTodo @Inject constructor() : GetFilteredTodoUseCase {
    override fun execute(
        allTodoList: List<TodoItem>,
        category: String,
        repeatType: String
    ): List<TodoItem> {
        return allTodoList.filter { item ->
            val categoryMatch = category == "전체" || item.category == category
            val repeatMatch = when (repeatType) {
                "전체" -> true
                "한 번" -> !item.hasRepeat
                "반복" -> item.hasRepeat
                else -> true
            }
            categoryMatch && repeatMatch
        }
    }
}
