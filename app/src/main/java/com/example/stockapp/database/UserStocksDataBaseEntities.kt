package com.example.stockapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.domain.UserStocksDataModel

@Entity
data class UserStocksEntity constructor(
    @PrimaryKey
    val symbol: String,
    val name: String)

// an extension function which converts from database objects to domain objects:
fun List<UserStocksEntity>.asDomainModel(): List<UserStocksDataModel> {
    return map {
        UserStocksDataModel (
            symbol = it.symbol,
            name = it.name)
    }
}

// an extension function which converts from database objects to domain objects:
fun UserStocksEntity.asDomainModel(): UserStocksDataModel {
    return let  {
        UserStocksDataModel (
            symbol = it.symbol,
            name = it.name)
    }
}