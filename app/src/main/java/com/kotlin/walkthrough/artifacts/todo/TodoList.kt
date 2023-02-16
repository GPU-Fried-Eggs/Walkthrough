package com.kotlin.walkthrough.artifacts.todo

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotlin.walkthrough.artifacts.todo.model.Todo

@Composable
fun TodoList(todos: List<Todo>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(todos) {
            Text(
                text = it.content,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Preview
@Composable
fun TodoListPreview() {
    TodoList(listOf(Todo(id = 1, content = "Preview")))
}
