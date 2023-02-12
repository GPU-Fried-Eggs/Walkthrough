package com.kotlin.walkthrough.ui.bmi.mvvm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class BmiViewModel: ViewModel() {
    var heightInput: String by mutableStateOf("")
    var weightInput: String by mutableStateOf("")

    val bmi: Float
        get() {
            return calculate()
        }

    private val height: Float
        get() {
            return heightInput.toFloatOrNull() ?: 0.0f
        }

    private val weight: Float
        get() {
            return weightInput.toFloatOrNull() ?: 0.0f
        }

    fun updateHeightInput(value: String) {
        heightInput = value
    }

    fun updateWeightInput(value: String) {
        weightInput = value
    }

    private fun calculate(): Float {
        return if (weight > 0 && height > 0) weight / height.pow(2) else 0.0f
    }
}