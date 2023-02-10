package com.kotlin.walkthrough.ui.electricity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.walkthrough.R
import kotlin.math.roundToInt

@Composable
fun Electricity() {
    var consumeInput: String by remember { mutableStateOf("") }
    var priceInput: Float by remember { mutableStateOf(0.25f) }
    var lowVAT: Boolean by remember { mutableStateOf(false) }
    val consume = consumeInput.toFloatOrNull() ?: 0.0f
    val price = priceInput
    val vat = if (lowVAT) 0.1f else 0.24f;
    val cost = ((price * 100.0f).roundToInt() / 100.0f * consume) * (1 + vat)

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // header
        Text(
            text = stringResource(R.string.electricity_header),
            fontSize = 24.sp,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )
        // consumption input field
        OutlinedTextField(
            value = consumeInput,
            label = { Text(stringResource(R.string.electricity_consumption)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp),
            onValueChange = {
                consumeInput = it.replace(",", ".")
            }
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp)
        ) {
            Text(stringResource(R.string.electricity_price, String.format("%.2f", price).replace(",", ".")))
            Slider(
                value = priceInput,
                onValueChange = { priceInput = it }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = lowVAT,
                onCheckedChange = { lowVAT = it }
            )
            Text(stringResource(R.string.electricity_vat))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {}) {
                Text(stringResource(R.string.electricity_result, String.format("%.2f", cost).replace(",", ".")))
            }
        }
    }
}

@Preview(name = "Cost of electricity")
@Composable
fun PreviewCalories() = Electricity()
