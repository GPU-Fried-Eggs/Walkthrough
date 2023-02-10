package com.kotlin.walkthrough.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Demo() {
    val spacingModifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)

    Column {
        Text(
            text = "Themed Title",
            style = MaterialTheme.typography.h5,
            modifier = spacingModifier
        )
        OutlinedTextField(
            value = "",
            label = { Text(text = "Themed Text Field") },
            modifier = spacingModifier,
            onValueChange = {},
        )
        Button(
            onClick = {},
            modifier = spacingModifier
        ) {
            Text(text = "Submit")
        }
    }

}

@Preview(name = "Themed")
@Composable
fun PreviewDemo() = Demo()
