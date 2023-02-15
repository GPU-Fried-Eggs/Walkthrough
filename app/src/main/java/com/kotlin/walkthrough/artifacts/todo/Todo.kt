package com.kotlin.walkthrough.artifacts.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Todo() {
    var startServer: Boolean by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                text = if (startServer) "Server is running" else "Server stopped"
            )
        }
    }
}

@Preview
@Composable
fun TodoPreview() {
    Todo()
}
