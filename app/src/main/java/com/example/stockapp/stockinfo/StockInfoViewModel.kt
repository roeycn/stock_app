package com.example.stockapp.stockinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.getDatabase
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.repository.StocksRepository
import com.example.stockapp.search.SearchResultDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StockInfoViewModel(stockData: SearchResultDao, application: Application) : ViewModel() {

    private val _selectedStock = MutableLiveData<SearchResultDao>()
    val selectedStock: LiveData<SearchResultDao>
        get() = _selectedStock

    private var _stockInfo = MutableLiveData<StockDataModel>()
    val stockInfo: LiveData<StockDataModel>
      get() = _stockInfo


    private var viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val stocksRepository = StocksRepository(database)

    init {
        _selectedStock.value = stockData

        coroutineScope.launch {
            stocksRepository.refreshStockData(stockData.stockSymbol)
        }
    }

   // val stock = stocksRepository.getStockData(stockData.stockSymbol)
   // val stocklist = stocksRepository.stocks

        val ss = stocksRepository.getStockLiveData(stockData.stockSymbol)

    fun setStockProperty(stock: StockDataModel) {
        _stockInfo.value = stock
    }

    /**
     * Factory for constructing StockInfoViewModel with parameter
     */
    class Factory(val stockData: SearchResultDao, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockInfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StockInfoViewModel(stockData , app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}