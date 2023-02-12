package com.kotlin.walkthrough.ui.alcometer

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.walkthrough.Debug
import com.kotlin.walkthrough.R
import com.kotlin.walkthrough.ui.calories.components.GenderSelection
import com.kotlin.walkthrough.ui.calories.components.Heading

@Composable
fun Alcometer() {
    var weightInput: String by remember { mutableStateOf("") }
    var genderInput: String by remember { mutableStateOf("Male") }
    var bottlesInput: String by remember { mutableStateOf("") }
    var hoursInput: String by remember { mutableStateOf("") }

    val weight = weightInput.toFloatOrNull() ?: 0.0f
    val bottles = bottlesInput.toIntOrNull() ?: 0
    val hours = hoursInput.toFloatOrNull() ?: 0.0f
    val litres = bottles * 0.33f
    val grams = litres * 8 * 4.5f
    val burning = weight / 10.0f

    val multiplier = when (genderInput) {
        "Male" -> 0.7f
        "Female" -> 0.6f
        else -> 0.0f
    }

    val result = if (weight > 0.0f && multiplier > 0.0f) (grams - burning * hours) / (weight * multiplier) else 0.0f

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Heading(stringResource(R.string.alcometer_header))
        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.alcometer_weight)) },
        )
        GenderSelection { genderInput = it }
        AlcometerSelectable(
            text = stringResource(R.string.alcometer_bottles),
            collection = (1..10).map { it.toString() }.toTypedArray(),
            value = bottlesInput,
            onValueChange = { bottlesInput = it }
        )
        AlcometerSelectable(
            text = stringResource(R.string.alcometer_hours),
            collection = (1..4).map { it.toString() }.toTypedArray(),
            value = hoursInput,
            onValueChange = { hoursInput = it }
        )
        Debug(
            text = {
                Text(stringResource(
                    R.string.alcometer_result,
                    String.format("%.2f", result).replace(",", ".")
                ))
            }
        ) {
            Button(
                onClick = it,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.alcometer_submit).uppercase(),
                    style = TextStyle(letterSpacing = 8.sp)
                )
            }
        }
    }
}

@Preview(name = "Alcometer calculator")
@Composable
fun PreviewAlcometer() = Alcometer()
