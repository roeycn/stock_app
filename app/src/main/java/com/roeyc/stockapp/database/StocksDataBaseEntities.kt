package com.roeyc.stockapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roeyc.stockapp.domain.StockDataModel

@Entity
data class DatabaseStock constructor(
    @PrimaryKey
    val symbol: String,
    val price: String,
    val volume: String,
    val latestTradingDay: String,
    val change: String)

// an extension function which converts from database objects to domain objects:
fun List<DatabaseStock>.asDomainModel(): List<StockDataModel> {
    return map {
        StockDataModel (
            symbol = it.symbol,
            price = it.price,
            volume = it.volume,
            latestTradingDay = it.latestTradingDay,
            change = it.change)
    }
}

// an extension function which converts from database objects to domain objects:
fun DatabaseStock.asDomainModel(): StockDataModel {
    return let  {
        StockDataModel (
            symbol = it.symbol,
            price = it.price,
            volume = it.volume,
            latestTradingDay = it.latestTradingDay,
            change = it.change)
    }
}

