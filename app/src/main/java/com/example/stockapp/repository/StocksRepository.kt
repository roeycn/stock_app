package com.example.stockapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.stockapp.database.StocksDatabase
import com.example.stockapp.database.asDomainModel
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.network.StockApi
import com.example.stockapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StocksRepository(private val database: StocksDatabase) {


    //Transformations.map is perfect for mapping the output of one LiveData to another type.
    //Use Transformation.map to convert your LiveData list of DatabaseStocks objects to domain Stock objects. Your completed StocksRepository class should look like this:
    val stocks: LiveData<List<StockDataModel>> = Transformations.map(database.stockDao.getStockData()) {
        it.asDomainModel()
    }

    // function to refresh the offline cache
    // Get the data from the network and then put it in the database
    suspend fun refreshStocks() {
        withContext(Dispatchers.IO) {
            val stocklastprice = StockApi.retrofitService.getStockLastPrice().await()
            database.stockDao.insertStock(stocklastprice.asDatabaseModel())
        }
    }
}