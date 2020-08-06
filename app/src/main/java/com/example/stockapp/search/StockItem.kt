package com.example.stockapp.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchResultDao(
    val stockSymbol: String,
    var stockName: String) : Parcelable

