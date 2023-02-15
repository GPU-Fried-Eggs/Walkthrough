package com.kotlin.walkthrough.artifacts.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.kotlin.walkthrough.artifacts.todo.server.HTTPServer

@Composable
fun Todo() {
    var startServer: Boolean by remember { mutableStateOf(false) }

    val httpServer = TodoServer()

    Column() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = startServer,
                onCheckedChange = {
                    if (!startServer) {
                        startServer = true
                        httpServer.start()
                    } else {
                        startServer = false
                        httpServer.stop()
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
