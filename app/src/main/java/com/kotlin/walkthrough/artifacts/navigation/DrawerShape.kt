package com.kotlin.walkthrough.artifacts.navigation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

// designed for DrawerShape in Scaffold
class DrawerShape(
    private val scale: Float = 1.0f,
    private val offset: Dp? = null,
    private val min: Dp? = null,
    private val max: Dp? = null
) : Shape {
    var width: Dp = Dp.Hairline
        private set

    override fun createOutline(
        size: Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        val currentWidth = clamp(
            value = size.width * scale + with(density) { offset?.toPx() ?: 0.0f },
            min = with(density) { min?.toPx() ?: 0.0f },
            max = with(density) { max?.toPx() ?: Float.POSITIVE_INFINITY }
        )

        width = with(density) { currentWidth.toDp() }

        return Outline.Rectangle(
            Rect(
                topLeft = Offset.Zero,
                bottomRight = Offset(currentWidth, size.height)
            )
        )
    }
}

fun clamp(value: Float, min: Float, max: Float): Float {
    return if (value < min) min else if (value > max) max else value
}
