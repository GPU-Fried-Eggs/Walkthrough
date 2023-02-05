package com.kotlin.walkthrough.ui.calories

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.kotlin.walkthrough.R

@Composable
fun Calories() {
    var heightInput: String by remember { mutableStateOf("") }
    var weightInput: String by remember { mutableStateOf("") }
    var ageInput: String by remember { mutableStateOf("") }
    var genderInput: String by remember { mutableStateOf("") }
    var activityInput: Float by remember { mutableStateOf(1f) }
    val height = heightInput.toFloatOrNull() ?: 0.0f
    val weight = weightInput.toFloatOrNull() ?: 0.0f
    val age = ageInput.toIntOrNull() ?: 0
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
        // header
        Text(
            text = stringResource(R.string.calories_header),
            fontSize = 24.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // height input field
            OutlinedTextField(
                value = heightInput,
                label = { Text(stringResource(R.string.calories_height)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onValueChange = {
                    heightInput = it.replace(",", ".")
                },
            )
            // weight input field
            OutlinedTextField(
                value = weightInput,
                label = { Text(stringResource(R.string.calories_weight)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                onValueChange = { weightInput = it.replace(",", ".") }
            )
        }
        // age input field
        OutlinedTextField(
            value = ageInput,
            label = { Text(stringResource(R.string.calories_age)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { ageInput = it.replace(",", ".") }
        )
        GenderSelection { genderInput = it }
        ActivityIntensity { activityInput = it }
        Text(
            text = stringResource(
                R.string.calories_result,
                String.format("%.2f", calories).replace(",", ".")
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun GenderSelection(onValueChange: (String) -> Unit) {
    var selected by remember { mutableStateOf("Male") }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selected == "Male",
                onClick = {
                    selected = "Male"
                    onValueChange("Male")
                }
            )
            Text(
                text = "Male",
                modifier = Modifier
                    .clickable(onClick = {
                        selected = "Male"
                        onValueChange("Male")
                    })
                    .padding(start = 4.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selected == "Female",
                onClick = {
                    selected = "Female"
                    onValueChange("Female")
                }
            )
            Text(
                text = "Female",
                modifier = Modifier
                    .clickable(onClick = {
                        selected = "Female"
                        onValueChange("Female")
                    })
                    .padding(start = 4.dp)
            )
        }
    }

}


@Composable
fun ActivityIntensity(onValueChange: (Float) -> Unit) {
    val intensityMap = mapOf("Rare" to 1.2f, "Light" to 1.375f, "Moderate" to 1.55f, "Heavy" to 1.725f)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Rare") }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val labelColor =
        if (expanded) MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high)
        else MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)

    val rotation: Float by animateFloatAsState(if (expanded) 180f else 0f)

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth().width(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = (if (expanded) 2 else 1).dp,
                    color =
                        if (expanded)
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity),
                    shape = TextFieldDefaults.OutlinedTextFieldShape
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0f),
                        shape = MaterialTheme.shapes.small
                    )
                    .onGloballyPositioned { textfieldSize = it.size.toSize() }
                    .clip(MaterialTheme.shapes.small)
                    .clickable {
                        expanded = !expanded
                        focusManager.clearFocus()
                    }
                    .padding(16.dp, 12.dp, 7.dp, 10.dp)
            ) {
                Column(Modifier.padding(end = 32.dp)) {
                    ProvideTextStyle(
                        value = MaterialTheme.typography.caption.copy(color = labelColor)
                    ) {
                        Text(stringResource(R.string.calories_intensity))
                    }
                    Text(
                        text = selectedText,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Change",
                    tint = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 4.dp)
                        .rotate(rotation)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) {
                textfieldSize.width.toDp()
            })
        ) {
            intensityMap.keys.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = label
                        expanded = false
                        intensityMap[label]?.let {
                            onValueChange(it)
                        }
                    }
                ) {
                    Text(text = label)
                }
            }
        }
    }
//    Column {
//        OutlinedTextField(
//            value = selectedText,
//            onValueChange = { selectedText = it },
//            modifier = Modifier
//                .fillMaxWidth()
//                .onGloballyPositioned { coordinates ->
//                    textfieldSize = coordinates.size.toSize()
//                },
//            readOnly = true,
//            label = { Text(stringResource(R.string.calories_intensity)) },
//            trailingIcon = {
//                Icon(
//                    imageVector = if (expanded)
//                        Icons.Filled.KeyboardArrowUp
//                    else
//                        Icons.Filled.KeyboardArrowDown,
//                    contentDescription = "Arrow Icon",
//                    modifier = Modifier.clickable { expanded = !expanded }
//                )
//            }
//        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.width(with(LocalDensity.current) {
//                textfieldSize.width.toDp()
//            })
//        ) {
//            intensityMap.keys.forEach { label ->
//                DropdownMenuItem(
//                    onClick = {
//                        selectedText = label
//                        expanded = false
//                        intensityMap[label]?.let {
//                            onValueChange(it)
//                        }
//                    }
//                ) {
//                    Text(text = label)
//                }
//            }
//        }
//    }
}

@Preview(name = "Calories calculator")
@Composable
fun PreviewCalories() = Calories()
