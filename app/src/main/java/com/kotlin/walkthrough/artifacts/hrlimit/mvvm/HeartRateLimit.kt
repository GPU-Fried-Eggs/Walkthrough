package com.kotlin.walkthrough.artifacts.hrlimit.mvvm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlin.walkthrough.R

@Composable
fun HeartRateLimit(hrLimitViewModel: HeartRateLimitViewModel = viewModel()) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // header
        Text(
            text = stringResource(R.string.hr_header),
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        OutlinedTextField(
            value = hrLimitViewModel.ageInput,
            onValueChange = { hrLimitViewModel.updateAgeInput(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.hr_age)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = stringResource(R.string.hr_result, hrLimitViewModel.lower, hrLimitViewModel.upper),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun HeartRateLimitPreview() {
    HeartRateLimit()
}
