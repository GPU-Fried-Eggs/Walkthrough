package com.kotlin.walkthrough.artifacts.todo

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlin.walkthrough.R
import com.kotlin.walkthrough.artifacts.todo.viewmodel.TodoUIState
import com.kotlin.walkthrough.artifacts.todo.viewmodel.TodoViewModel

@Composable
fun Todo(todoViewModel: TodoViewModel = viewModel()) {
    var startServer: Boolean by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // header
        Text(
            text = stringResource(R.string.todo_header),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier.width(200.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = startServer,
                    onCheckedChange = {
                        if (!startServer) {
                            startServer = true
                            TodoServer.instance.start()
                        } else {
                            startServer = false
                            TodoServer.instance.stop()
                        }
                    }
                )
                Text(
                    text = stringResource(
                        if (startServer)
                            R.string.todo_server_start
                        else
                            R.string.todo_server_stop
                    )
                )
            }

        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp
        )
        when(todoViewModel.todoUIState) {
            is TodoUIState.Loading -> Text("Loading")
            is TodoUIState.Success -> TodoList((todoViewModel.todoUIState as TodoUIState.Success).todos)
            is TodoUIState.Error -> Text("Error retrieving data from api")
        }
    }
}

@Preview
@Composable
fun TodoPreview() {
    Todo()
}
