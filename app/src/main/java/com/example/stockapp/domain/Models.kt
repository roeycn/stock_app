package com.example.stockapp.domain

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
                 val price: String) {

}