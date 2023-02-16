package com.kotlin.walkthrough.artifacts.todo.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.walkthrough.artifacts.todo.TodoApi
import com.kotlin.walkthrough.artifacts.todo.model.Todo
import kotlinx.coroutines.launch

sealed interface TodoUIState {
    data class Success(val todos: List<Todo>): TodoUIState

    object Error: TodoUIState

    object Loading: TodoUIState
}

class TodoViewModel: ViewModel() {
    var todoUIState: TodoUIState by mutableStateOf(TodoUIState.Loading)
        private set

    init {
        getTodoList()
    }

    private fun getTodoList() {
        viewModelScope.launch {
            while (todoUIState === TodoUIState.Loading || todoUIState === TodoUIState.Error) {
                var todoApi: TodoApi?
                try {
                    todoApi = TodoApi.getInstance()
                    todoUIState = TodoUIState.Success(todoApi.getTodos())
                } catch (e: Exception) {
                    Log.d("TodoViewModel", "${e.message}")
                    todoUIState = TodoUIState.Error
                }
            }
        }
    }
}