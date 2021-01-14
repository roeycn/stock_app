package com.roeyc.stockapp.network

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Gets Mars real estate property information from the Mars API Retrofit service and updates the
 * [StockSearchProperty] and [MarsApiStatus] [LiveData]. The Retrofit service returns a coroutine
 * Deferred, which we await to get the result of the transaction.
 * @param filter the [MarsApiFilter] that is sent as part of the web server request
 */
@Parcelize
data class StockSearchProperty(
    @Json(name = "bestMatches")
    val metaData: List<StockSearchPropertyMetaData>) : Parcelable
