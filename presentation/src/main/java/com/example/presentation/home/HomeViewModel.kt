package com.example.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.todo.UseCaseGetFilteredTodo
import com.example.domain.todo.model.RepeatType
import com.example.domain.todo.model.TodoCategory
import com.example.domain.todo.model.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCaseGetFilteredTodo: UseCaseGetFilteredTodo
) : ViewModel() {

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> = _todoList

    // todo 테스트를 위한 임시 코드입니다
    private val allTodoList = listOf(
        TodoItem("산책 가기", true, false, true, TodoCategory.LOCATION),
        TodoItem("카페 가기", false, true, true, TodoCategory.LOCATION),
        TodoItem("헬스장 등록", true, true, true, TodoCategory.LOCATION),
        TodoItem("마트 장보기", false, false, true, TodoCategory.LOCATION),
        TodoItem("공부하기", false, false, true, TodoCategory.TIME, "11:00"),
        TodoItem("요가 클래스", true, true, true, TodoCategory.TIME, "23:20"),
        TodoItem("영어 회화 수업", true, false, true, TodoCategory.TIME, "10:00"),
        TodoItem("독서", false, true, true, TodoCategory.TIME, "06:30")
    )


    var selectedCategory: String = TodoCategory.LOCATION.value
    var selectedRepeatType: String = RepeatType.ALL.value


    fun filterTodoList() {
        _todoList.value = useCaseGetFilteredTodo.execute(
            allTodoList, selectedCategory, selectedRepeatType
        )
    }
}
