package com.kotlin.walkthrough.ui.calories.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.kotlin.walkthrough.R

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
