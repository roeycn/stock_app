package com.example.qaapp.network

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Gets Mars real estate property information from the Mars API Retrofit service and updates the
 * [StockDailyProperty_first] and [MarsApiStatus] [LiveData]. The Retrofit service returns a coroutine
 * Deferred, which we await to get the result of the transaction.
 * @param filter the [MarsApiFilter] that is sent as part of the web server request
 */
@Parcelize
data class StockDailyPropertyMetaData(
    @Json(name = "1. Information") val information: String,
    @Json(name = "2. Symbol") val Symbol: String,
    @Json(name = "3. Last Refreshed") val lastRefreshed: String,
    @Json(name = "4. Interval") val interval: String,
    @Json(name = "5. Output Size") val outputSize: String,
    @Json(name = "6. Time Zone") val timeZone: String) : Parcelable
