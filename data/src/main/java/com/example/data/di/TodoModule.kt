package com.example.data.di

import com.example.data.todo.GetFilteredTodoUseCaseImpl
import com.example.domain.todo.GetFilteredTodoUseCase
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
        impl: GetFilteredTodoUseCaseImpl
    ): GetFilteredTodoUseCase
}