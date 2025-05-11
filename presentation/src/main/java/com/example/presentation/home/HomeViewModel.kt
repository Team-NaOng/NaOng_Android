package com.example.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.todo.UseCaseGetFilteredTodo
import com.example.domain.todo.TodoItem
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
        TodoItem("산책 가기", true, false, true, "위치"),
        TodoItem("카페 가기", false, true, true, "위치"),
        TodoItem("헬스장 등록", true, true, true, "위치"),
        TodoItem("마트 장보기", false, false, true, "위치"),
        TodoItem("공부하기", false, false, true, "시간"),
        TodoItem("요가 클래스", true, true, true, "시간"),
        TodoItem("영어 회화 수업", true, false, true, "시간"),
        TodoItem("독서", false, true, true, "시간")
    )

    var selectedMainCategory = "전체"
    var selectedSubCategory = "전체"

    fun filterTodoList() {
        _todoList.value = useCaseGetFilteredTodo.execute(
            allTodoList, selectedMainCategory, selectedSubCategory
        )
    }
}
