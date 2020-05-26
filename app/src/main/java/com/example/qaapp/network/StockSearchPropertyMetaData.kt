package com.example.qaapp.network

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Gets Mars real estate property information from the Mars API Retrofit service and updates the
 * [StockSearchPropertyMetaData] and [MarsApiStatus] [LiveData]. The Retrofit service returns a coroutine
 * Deferred, which we await to get the result of the transaction.
 * @param filter the [MarsApiFilter] that is sent as part of the web server request
 */
@Parcelize
data class StockSearchPropertyMetaData(
    @Json(name = "1. symbol") val symbol: String,
    @Json(name = "2. name") val name: String,
    @Json(name = "3. type") val type: String,
    @Json(name = "4. region") val region: String,
    @Json(name = "5. marketOpen") val marketOpen: String,
    @Json(name = "6. marketClose") val marketClose: String,
    @Json(name = "7. timezone") val timezone: String,
    @Json(name = "8. currency") val currency: String,
    @Json(name = "9. matchScore") val matchScore: String) : Parcelable
