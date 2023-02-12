package com.kotlin.walkthrough.ui.sensor

import android.content.Context
import android.hardware.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

@Composable
fun Sensors() {
    val ctx = LocalContext.current
    val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

    Column {
        Text(
            text = "Available sensors",
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            color = MaterialTheme.colors.primary,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        LazyColumn {
            deviceSensors.forEach { sensor ->
                item {
                    Divider()
                    Text(
                        text = sensor.name,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SensorsPreview() {
    Sensors()
}
