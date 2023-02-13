package com.kotlin.walkthrough.ui.location

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kotlin.walkthrough.ui.location.model.LocationLiveData

class LocationViewModel(context: Context): ViewModel() {
    private val locationLiveData = LocationLiveData(context)

    fun getLocationLiveData() = locationLiveData
}
