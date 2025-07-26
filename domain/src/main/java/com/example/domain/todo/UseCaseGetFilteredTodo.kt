package com.example.domain.todo

import com.example.domain.todo.model.TodoItem

interface UseCaseGetFilteredTodo {
    fun execute(
        allTodoList: List<TodoItem>,
        category: String,
        repeatType: String
    ): List<TodoItem>
}
