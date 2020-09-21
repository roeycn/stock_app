package com.example.stockapp.domain

import com.example.stockapp.database.DatabaseStock
import com.example.stockapp.database.UserStocksEntity
import com.example.stockapp.network.StockLastPriceContainer

/**
* Domain objects are plain Kotlin data classes that represent the things in our app. These are the
* objects that should be displayed on screen, or manipulated by the app.
*
* @see database for objects that are mapped to the database
* @see network for objects that parse or prepare network calls
*/

/**
 * Stock represent a stock data
 */
data class StockDataModel(
                 val symbol: String,
                 val price: String,
                 val volume: String,
                 val latestTradingDay: String,
                 val change: String) {
}

data class UserStocksDataModel(
    val symbol: String,
    val name: String,
    var value: Int) {
}

// an extension function that converts from data transfer objects to database objects:
fun  UserStocksDataModel.asDatabaseModel(): UserStocksEntity {
        return UserStocksEntity(
            symbol = symbol,
            name = name,
            value = value
        )

}