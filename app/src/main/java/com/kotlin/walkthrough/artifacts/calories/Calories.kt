package com.kotlin.walkthrough.artifacts.calories

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotlin.walkthrough.Debug
import com.kotlin.walkthrough.R

@Composable
fun Calories() {
    var paramsInput: Map<String, String> by remember { mutableStateOf(mapOf()) }
    var genderInput: String by remember { mutableStateOf("Male") }
    var activityInput: Float by remember { mutableStateOf(1f) }

    val height = paramsInput.getOrDefault("height", "").toFloatOrNull() ?: 0.0f
    val weight = paramsInput.getOrDefault("weight", "").toFloatOrNull() ?: 0.0f
    val age = paramsInput.getOrDefault("age", "").toIntOrNull() ?: 0

    // Harris–Benedict equation https://en.wikipedia.org/wiki/Harris%E2%80%93Benedict_equation
    val bmr: Float = when (genderInput) {
        "Male" -> 66.473f + (13.7516f * weight) + (5.0033f * height) - (6.755f * age)
        "Female" -> 655.0955f + (9.5634f * weight) + (1.8496f * height) - (4.6756f * age)
        else -> 0.0f
    }

    val calories = if (height > 0 && weight > 0 && age > 0) bmr * activityInput else 0.0f

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CaloriesHeading(stringResource(R.string.calories_header))
        CaloriesParams(paramsInput) { paramsInput = it }
        CaloriesGender { genderInput = it }
        CaloriesActivity { activityInput = it }
        Debug(
            text = {
                Text(stringResource(
                    R.string.calories_result,
                    String.format("%.2f", calories).replace(",", ".")
                ))
            }
        ) {
            Button(
                onClick = it,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(40.dp)
            ) {
                Text(stringResource(R.string.alcometer_submit))
            }
        }
    }
}

@Preview
@Composable
fun CaloriesPreview() {
    Calories()
}
