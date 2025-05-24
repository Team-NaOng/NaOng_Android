package com.example.domain.todo

interface UseCaseGetFilteredTodo {
    fun execute(
        allTodoList: List<TodoItem>,
        category: String,
        repeatType: String
    ): List<TodoItem>
}
