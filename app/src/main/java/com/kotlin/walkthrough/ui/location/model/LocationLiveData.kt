package com.kotlin.walkthrough.ui.location.model

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationServices

class LocationLiveData(private val context: Context): LiveData<Coordinates>() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onActive() {
        super.onActive()
        getLocationData()
    }

    fun getLocationData() {
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
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

    private fun setLocationData(location: Location?) {
        location?.let {
            value = Coordinates(it.latitude, it.longitude)
        }
    }
}
