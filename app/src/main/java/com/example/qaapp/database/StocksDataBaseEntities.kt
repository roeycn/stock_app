package com.example.qaapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qaapp.domain.StockDataModel
import com.example.qaapp.network.StockLastPriceData

@Entity
data class DatabaseStock constructor(
    @PrimaryKey
    val symbol: String,
    val price: String)

// an extension function which converts from database objects to domain objects:
fun List<DatabaseStock>.asDomainModel(): List<StockDataModel> {
    return map {
        StockDataModel (
            symbol = it.symbol,
            price = it.price)
    }
}