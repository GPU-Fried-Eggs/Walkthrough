package com.kotlin.walkthrough.ui.bmi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.kotlin.walkthrough.R
import com.kotlin.walkthrough.ui.calories.Calories
import kotlin.math.pow

@Composable
fun Bmi() {
    var heightInput: String by remember { mutableStateOf("") }
    var weightInput: String by remember { mutableStateOf("") }
    val height = heightInput.toFloatOrNull() ?: 0.0f
    val weight = weightInput.toIntOrNull() ?: 0
    val bmi = if (weight > 0 && height > 0) weight / height.pow(2) else 0.0

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // header
        Text(
            text = stringResource(R.string.bmi_header),
            fontSize = 24.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
        // height input field
        OutlinedTextField(
            value = heightInput,
            label = { Text(stringResource(R.string.bmi_height)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp),
            onValueChange = {
                heightInput = it.replace(",", ".")
            }, // normalize string
        )
        // weight input field
        OutlinedTextField(
            value = weightInput,
            label = { Text(stringResource(R.string.bmi_weight)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp),
            onValueChange = { weightInput = it.replace(",", ".") }
        )
        Text(
            text = stringResource(R.string.bmi_result, String.format("%.2f", bmi).replace(",", ".")),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

@Preview(name = "Bmi calculator")
@Composable
fun PreviewBmi() = Bmi()
