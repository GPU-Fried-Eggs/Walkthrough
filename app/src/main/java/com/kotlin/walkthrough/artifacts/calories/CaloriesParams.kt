package com.kotlin.walkthrough.artifacts.calories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotlin.walkthrough.R

@Composable
fun CaloriesParams(Inputs: Map<String, String>, onValueChange: (Map<String, String>) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // height input field
        OutlinedTextField(
            value = Inputs["height"] ?: "",
            label = { Text(stringResource(R.string.calories_height)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onValueChange = {
                val current = Inputs + mapOf("height" to it.replace(",", "."))
                onValueChange(current)
            },
        )
        // weight input field
        OutlinedTextField(
            value = Inputs["weight"] ?: "",
            label = { Text(stringResource(R.string.calories_weight)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            onValueChange = {
                val current = Inputs + mapOf("weight" to it.replace(",", "."))
                onValueChange(current)
            }
        )
    }
    // age input field
    OutlinedTextField(
        value = Inputs["age"] ?: "",
        label = { Text(stringResource(R.string.calories_age)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {
            val current = Inputs + mapOf("age" to it.replace(",", "."))
            onValueChange(current)
        }
    )
}

@Preview
@Composable
fun CaloriesParamsPreview() {
    CaloriesParams(mapOf(), {})
}
