package com.kotlin.walkthrough.ui.calories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CaloriesGender(onValueChange: (String) -> Unit) {
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

@Preview
@Composable
fun CaloriesGenderPreview() {
    CaloriesGender({})
}
