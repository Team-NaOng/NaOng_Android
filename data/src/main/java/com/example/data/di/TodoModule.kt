package com.example.data.di

import com.example.data.todo.ImplementUseCaseGetFilteredTodo
import com.example.domain.todo.UseCaseGetFilteredTodo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodoModule {

    @Binds
    @Singleton
    abstract fun bindGetFilteredTodoUseCase(
        impl: ImplementUseCaseGetFilteredTodo
    ): UseCaseGetFilteredTodo
}