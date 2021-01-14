package com.roeyc.stockapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roeyc.stockapp.domain.UserStocksDataModel

@Entity
data class UserStocksEntity constructor(
    @PrimaryKey
    val symbol: String,
    val name: String,
    val price: String,
    var value: Int)

// an extension function which converts from database objects to domain objects:
fun List<UserStocksEntity>.asDomainModel(): List<UserStocksDataModel> {
    return map {
        UserStocksDataModel (
            symbol = it.symbol,
            name = it.name,
            price = it.price,
            value = it.value)
    }
}

// an extension function which converts from database objects to domain objects:
fun UserStocksEntity.asDomainModel(): UserStocksDataModel {
    return let  {
        UserStocksDataModel (
            symbol = it.symbol,
            name = it.name,
            price = it.price,
            value = it.value)
    }
}