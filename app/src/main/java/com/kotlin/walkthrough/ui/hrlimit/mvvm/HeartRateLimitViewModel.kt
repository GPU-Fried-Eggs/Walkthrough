package com.kotlin.walkthrough.ui.hrlimit.mvvm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class HeartRateLimitViewModel: ViewModel() {
    var ageInput by mutableStateOf("")

    val upper: Float
        get() {
            return calculate(0.85f)
        }

    val lower: Float
        get() {
            return calculate(0.65f)
        }

    private val age: Int
        get() {
            return ageInput.toIntOrNull() ?: 0
        }

    fun updateAgeInput(value: String) {
        ageInput = value
    }

    private fun calculate(multiplier: Float): Float {
        return if (age > 0) (220 - age) * multiplier else 0.0f
    }
}
