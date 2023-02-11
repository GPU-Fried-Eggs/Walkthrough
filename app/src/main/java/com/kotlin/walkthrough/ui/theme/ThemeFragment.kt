package com.kotlin.walkthrough.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotlin.walkthrough.ui.theme.venti.VentiTheme

@Composable
fun ThemeFragment() {
    val spacingModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)

    VentiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
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
    }
}

@Preview(name = "Themed")
@Composable
fun ThemePreview() {
    ThemeFragment()
}
