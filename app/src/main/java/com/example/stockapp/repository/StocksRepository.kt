package com.example.stockapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.stockapp.database.DatabaseStock
import com.example.stockapp.database.StocksDatabase
import com.example.stockapp.database.asDomainModel
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.network.StockApi
import com.example.stockapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// A Repository manages queries and allows you to use multiple backends.
// In the most common example, the Repository implements the logic for deciding whether to fetch data
// from a network or use results cached in a local database.
class StocksRepository(private val database: StocksDatabase) {

    private val alphavantageFreeApiKey = "CITUC4CO27CTCF4D"


    //Transformations.map is perfect for mapping the output of one LiveData to another type.
    //Use Transformation.map to convert your LiveData list of DatabaseStocks objects to domain Stock objects.
    // Your completed StocksRepository class should look like this:
    val stocks: LiveData<List<StockDataModel>> = Transformations.map(database.stockDao.getAllStocksData()) {
        it.asDomainModel()
    }

    fun getStockLiveData(symbol: String): LiveData<StockDataModel> {
        val stock: LiveData<StockDataModel> = Transformations.map(database.stockDao.getStockLiveData(symbol)) {
            it?.asDomainModel()
        }
        return stock
    }

    fun getStockData(symbol: String): StockDataModel {
        val stock: StockDataModel = database.stockDao.getStockData(symbol) as StockDataModel
        return stock
    }

    // function to refresh the offline cache
    // Get the data from the network and then put it in the database
    suspend fun refreshStockData(symbol: String) {
        withContext(Dispatchers.IO) {
            val stocklastprice = StockApi.retrofitService.getStockLastPrice(symbol, alphavantageFreeApiKey).await()
            database.stockDao.insertStock(stocklastprice.asDatabaseModel())
        }
    }
}