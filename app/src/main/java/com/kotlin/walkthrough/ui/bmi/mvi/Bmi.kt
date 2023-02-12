package com.kotlin.walkthrough.ui.bmi.mvi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.walkthrough.R
import kotlin.math.pow

@Composable
fun Bmi() {
    var heightInput: String by remember { mutableStateOf("") }
    var weightInput: String by remember { mutableStateOf("") }

    val height = heightInput.toFloatOrNull() ?: 0.0f
    val weight = weightInput.toFloatOrNull() ?: 0.0f

    val bmi = if (weight > 0 && height > 0) weight / height.pow(2) else 0.0

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // header
        Text(
            text = stringResource(R.string.bmi_header),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        // height input field
        OutlinedTextField(
            value = heightInput,
            onValueChange = { heightInput = it.replace(",", ".") },
            modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp),
            label = { Text(stringResource(R.string.bmi_height)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        // weight input field
        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it.replace(",", ".") },
            modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp),
            label = { Text(stringResource(R.string.bmi_weight)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Text(
            text = stringResource(R.string.bmi_result, String.format("%.2f", bmi).replace(",", ".")),
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun BmiPreview() {
    Bmi()
}
