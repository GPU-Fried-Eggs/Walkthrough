package com.kotlin.walkthrough

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Debug(text: @Composable () -> Unit, content: @Composable (() -> Unit) -> Unit) {
    var showDebug: Boolean by remember { mutableStateOf(false) }

    content { showDebug = true }

    if (showDebug) {
        AlertDialog(
            onDismissRequest = { showDebug = false },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton({ showDebug = false }) {
                        Text("Done")
                    }
                }
            },
            modifier = Modifier.padding(8.dp),
            title = {
                Text(
                    text = "Result",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5
                )
            },
            text = { text() }
        )
    }
}