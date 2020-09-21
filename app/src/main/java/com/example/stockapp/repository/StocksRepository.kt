package com.example.stockapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.stockapp.database.DatabaseStock
import com.example.stockapp.database.StocksDatabase
import com.example.stockapp.database.UserStocksEntity
import com.example.stockapp.database.asDomainModel
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.domain.UserStocksDataModel
import com.example.stockapp.domain.asDatabaseModel
import com.example.stockapp.mars.MarsApiStatus
import com.example.stockapp.network.MarsApi
import com.example.stockapp.network.StockApi
import com.example.stockapp.network.StockLastPriceContainer
import com.example.stockapp.network.asDatabaseModel
import com.example.stockapp.stockinfo.StockInfoApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// A Repository manages queries and allows you to use multiple backends.
// In the most common example, the Repository implements the logic for deciding whether to fetch data
// from a network or use results cached in a local database.
class StocksRepository (private val database: StocksDatabase) {

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

    suspend fun insertStockToUserStocks(model: UserStocksDataModel) {
        withContext(Dispatchers.IO) {
            database.stockDao.insertStockToUserStocks(model.asDatabaseModel())
        }
    }

    // function to refresh the offline cache
    // Get the data from the network and then put it in the database
    suspend fun refreshStockData(symbol: String) {
        withContext(Dispatchers.IO) {
            val stocklastprice = StockApi.retrofitService.getStockLastPrice(symbol, alphavantageFreeApiKey).await()
            database.stockDao.insertStock(stocklastprice.asDatabaseModel())
        }
    }

    val userStocks: LiveData<List<UserStocksDataModel>> = Transformations.map(database.stockDao.getUserStocks()) {
        it.asDomainModel()
    }

    suspend fun removeStockFromUserStocks(model: UserStocksDataModel) {
        withContext(Dispatchers.IO) {
            database.stockDao.removeStockFromUserStocks(model.asDatabaseModel())
        }
    }

}