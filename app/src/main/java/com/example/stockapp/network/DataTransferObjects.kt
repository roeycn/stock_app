package com.example.stockapp.network

import com.example.stockapp.database.DatabaseStock
import com.example.stockapp.domain.StockDataModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class StockLastPriceContainer(
    @Json(name = "Global Quote") val stockLastPriceData: StockLastPriceData)

@JsonClass(generateAdapter = true)
data class StockLastPriceData(
    @Json(name = "01. symbol") val symbol: String,
    @Json(name = "05. price")val price: String)


/**
 * Convert Network results to database objects
 */
fun StockLastPriceContainer.asDomainModel(): StockDataModel {
    return stockLastPriceData.let {
        StockDataModel(
            symbol = it.symbol,
            price = it.price)
    }
}

/**
 * Convert Network results to database objects
 */
//fun StockLastPriceContainer.asDomainModel(): List<StockDataModel> {
//    return stockLastPriceData.map {
//        StockDataModel(
//            symbol = it.symbol,
//            price = it.price)
//    }
//}




// an extension function that converts from data transfer objects to database objects:
//fun StockLastPriceContainer.asDatabaseModel(): Array<DatabaseStock>{
//    return stockLastPriceData.map {
//        DatabaseStock(
//            symbol = it.symbol,
//            price = it.price)
//    }.toTypedArray()
//}

// an extension function that converts from data transfer objects to database objects:
fun StockLastPriceContainer.asDatabaseModel():DatabaseStock{
    return stockLastPriceData.let {
        DatabaseStock(
            symbol = it.symbol,
            price = it.price)
    }
}