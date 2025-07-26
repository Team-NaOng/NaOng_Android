package com.example.data.todo

import com.example.domain.todo.UseCaseGetFilteredTodo
import com.example.domain.todo.model.RepeatType
import com.example.domain.todo.model.TodoCategory
import com.example.domain.todo.model.TodoItem
import javax.inject.Inject

class ImplementUseCaseGetFilteredTodo @Inject constructor() : UseCaseGetFilteredTodo {

    override fun execute(
        allTodoList: List<TodoItem>,
        category: String,
        repeatType: String
    ): List<TodoItem> {
        val categoryEnum = TodoCategory.fromString(category)
        val repeatEnum = RepeatType.fromString(repeatType)

        return allTodoList.filter { item ->
            val categoryMatch = item.category == categoryEnum.value

            val repeatMatch = when (repeatEnum) {
                RepeatType.ALL -> true
                RepeatType.ONCE -> !item.hasRepeat
                RepeatType.REPEAT -> item.hasRepeat
            }

            categoryMatch && repeatMatch
        }
    }
}
