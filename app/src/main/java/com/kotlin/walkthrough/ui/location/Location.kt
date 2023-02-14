package com.kotlin.walkthrough.ui.location

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.kotlin.walkthrough.R

@Composable
fun Location(
    context: Context = LocalContext.current,
    locationViewModel: LocationViewModel = LocationViewModel(context)
) {
    val location by locationViewModel.getLocationLiveData().observeAsState()
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
        locationViewModel.getLocationLiveData().getLocationData()
        show = it
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                locationViewModel.getLocationLiveData().getLocationData()
                show = true
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(stringResource(R.string.location_button))
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f).align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (enableGService) {
                Text(location?.latitude.toString())
                Text(location?.longitude.toString())
            } else {
                Text(stringResource(R.string.location_noGMS))
            }
        }
    }
}

@Preview
@Composable
fun LocationPreview() {
    Location()
}
