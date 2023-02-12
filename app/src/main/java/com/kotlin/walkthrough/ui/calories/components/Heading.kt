package com.kotlin.walkthrough.ui.calories.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Heading(title: String) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
}
