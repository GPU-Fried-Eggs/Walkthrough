package com.kotlin.walkthrough.ui.location

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kotlin.walkthrough.R

@Composable
fun Location(
    context: Context = LocalContext.current,
    locationViewModel: LocationViewModel = LocationViewModel(context)
) {
    val location by locationViewModel.getLocationLiveData().observeAsState()
    var show: Boolean by remember { mutableStateOf(false) }

    LocationRequest(context, show) {
        locationViewModel.getLocationLiveData().getLocationData()
        show = it
    }

    Column {
        Button(
            onClick = {
                locationViewModel.getLocationLiveData().getLocationData()
                show = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.location_button))
        }
        Text(location?.latitude.toString())
        Text(location?.longitude.toString())
    }
}

@Preview
@Composable
fun LocationPreview() {
    Location()
}
