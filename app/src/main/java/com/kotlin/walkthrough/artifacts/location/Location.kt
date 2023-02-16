package com.kotlin.walkthrough.artifacts.location

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.kotlin.walkthrough.R
import com.kotlin.walkthrough.artifacts.location.viewmodel.LocationViewModel

@Composable
fun Location(
    context: Context = LocalContext.current,
    locationViewModel: LocationViewModel = LocationViewModel(context)
) {
    val location by locationViewModel.getLocationLiveData().observeAsState()
    var enableRealtime: Boolean by remember { mutableStateOf(false) }
    var enableGService: Boolean by remember { mutableStateOf(false) }
    var show: Boolean by remember { mutableStateOf(false) }

    GoogleApiAvailability.getInstance().apply {
        enableGService =
            when(isGooglePlayServicesAvailable(context)) {
                ConnectionResult.SUCCESS -> true
                else -> false
            }
    }

    LocationRequest(context, show) {
        locationViewModel.getLocationLiveData().getLastLocationData()
        show = it
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.location_header),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        Icon(
            painter = painterResource(R.drawable.ic_nav_location),
            contentDescription = stringResource(R.string.location_header),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(128.dp)
                .align(Alignment.CenterHorizontally)
                .drawWithCache {
                    val brush = Brush.linearGradient(
                        listOf(
                            Color(0x809E82F0),
                            Color(0x8042A5F5)
                        )
                    )
                    onDrawBehind {
                        drawRoundRect(
                            brush,
                            cornerRadius = CornerRadius(10.dp.toPx())
                        )
                    }
                },
            tint = MaterialTheme.colors.surface
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (enableGService) {
                Text(
                    text = stringResource(
                        id = R.string.location_latitude,
                        location?.latitude.let {
                            when (it) {
                                null -> "Loading"
                                else -> "%.3f".format(it)
                            }
                        }
                    )
                )
                Text(
                    text = stringResource(
                        id = R.string.location_longitude,
                        location?.longitude.let {
                            when (it) {
                                null -> "Loading"
                                else -> "%.3f".format(it)
                            }
                        }
                    )
                )
            } else {
                Text(stringResource(R.string.location_noGMS))
            }
        }
        Button(
            onClick = {
                locationViewModel.getLocationLiveData().getLastLocationData()
                show = true
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.location_last_button))
        }
        Button(
            onClick = {
                locationViewModel.getLocationLiveData().getLocationData()
                show = true
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.location_current_button))
        }
        Button(
            onClick = {
                locationViewModel.getLocationLiveData().getTraceLocationData(!enableRealtime)
                enableRealtime = !enableRealtime
                show = true
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(if (enableRealtime) R.string.location_trace_on_button else R.string.location_trace_off_button))
        }
    }
}

@Preview
@Composable
private fun LocationPreview() {
    Location()
}
