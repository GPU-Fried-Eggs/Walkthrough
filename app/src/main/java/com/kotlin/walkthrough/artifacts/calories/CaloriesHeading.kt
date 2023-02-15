package com.kotlin.walkthrough.artifacts.calories

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CaloriesHeading(title: String) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
}

@Preview
@Composable
private fun CaloriesHeadingPreview() {
    CaloriesHeading("Preview")
}
