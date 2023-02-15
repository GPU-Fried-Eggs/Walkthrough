package com.kotlin.walkthrough.artifacts.location.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

class LocationLiveData(private val context: Context): LiveData<Coordinates>() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            setLocationData(result.lastLocation)
        }
    }

    override fun onActive() {
        super.onActive()
        getLocationData()
    }

    fun getLastLocationData() {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener { location ->
                    location.also {
                        setLocationData(it)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    fun getLocationData() {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                    override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location ->
                    location.also {
                        setLocationData(it)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
        }
    }

    fun getTraceLocationData(enable: Boolean) {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (enable) {
                fusedLocationClient
                    .requestLocationUpdates(
                        LocationRequest.Builder(3 * 1000L).build(),
                        locationCallback,
                        Looper.myLooper()
                    )
            } else {
                fusedLocationClient
                    .removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun setLocationData(location: Location?) {
        location?.let {
            value = Coordinates(it.latitude, it.longitude)
        }
    }
}
