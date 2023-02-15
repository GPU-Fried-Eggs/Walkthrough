package com.kotlin.walkthrough.artifacts.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.kotlin.walkthrough.R

@Composable
fun LocationRequest(context: Context = LocalContext.current, enable: Boolean, onEnable: (Boolean) -> Unit) {
    var enableLocationPermission: Boolean by remember { mutableStateOf(false) }
    val requirePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.values.all { value -> value }) {
            Log.d("LocationRequest", "${it.keys.joinToString()} PERMISSION GRANTED")
            enableLocationPermission = true
            onEnable(false)
        } else {
            it.forEach { entry ->
                Log.d(
                    "LocationRequest",
                    "${entry.key} ${if (entry.value) "PERMISSION GRANTED" else "PERMISSION DENIED"}"
                )
            }
        }
    }

    if (enable && !enableLocationPermission && filterPermissions(requirePermissions, context).isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { onEnable(false) },
            title = { Text(stringResource(R.string.location_request_header)) },
            text = { Text(stringResource(R.string.location_request_content)) },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { requirePermissions(requirePermissions, context, launcher) }
                    ) {
                        Text(text = "Enable")
                    }
                }
            }
        )
    }
}

private fun requirePermissions(
    permissions: Array<String>,
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {
    val required = filterPermissions(permissions, context)

    if (required.isNotEmpty()) {
        launcher.launch(required.toTypedArray())
    }
}

private fun filterPermissions(permissions: Array<String>, context: Context): List<String> {
    return permissions.filter {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, it) -> false
            else -> true // failed test
        }
    }
}

@Preview
@Composable
private fun LocationRequestPreview() {
    var show: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { show = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Show Location Request Preview")
        }
    }

    LocationRequest(enable = show) {
        show = it
    }
}
