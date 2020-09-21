package com.example.stockapp.stockinfo

import android.app.Application
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.R
import com.example.stockapp.ViewDimensionChangeHelper
import com.example.stockapp.database.Cars
import com.example.stockapp.database.getDatabase
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.domain.UserStocksDataModel
import com.example.stockapp.mars.MarsApiStatus
import com.example.stockapp.network.StockApi
import com.example.stockapp.repository.StocksRepository
import com.example.stockapp.search.SearchResultDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.internal.wait

enum class StockInfoApiStatus { LOADING, ERROR, DONE }

class StockInfoViewModel(stockData: SearchResultDao, application: Application) : ViewModel() {

    val dimensionsHelper = ViewDimensionChangeHelper()

    private val _selectedStock = MutableLiveData<SearchResultDao>()
    val selectedStock: LiveData<SearchResultDao>
        get() = _selectedStock

    private var _stockInfo = MutableLiveData<StockDataModel>()
    val stockInfo: LiveData<StockDataModel>
      get() = _stockInfo

    private val _addToStockList = MutableLiveData<Boolean>()
    val addToStockList: LiveData<Boolean>
        get() = _addToStockList

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<StockInfoApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<StockInfoApiStatus>
        get() = _status


    private var viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val stocksRepository = StocksRepository(database)

    init {
        _selectedStock.value = stockData

        val alphavantageFreeApiKey = "CITUC4CO27CTCF4D"

        _status.value = StockInfoApiStatus.LOADING
        coroutineScope.launch {
            stocksRepository.refreshStockData(stockData.stockSymbol);
        }

    }

    val ss = stocksRepository.getStockLiveData(stockData.stockSymbol)

    fun setStockProperty(stock: StockDataModel) {
        _stockInfo.value = stock
        _status.value = StockInfoApiStatus.DONE
    }

    fun addToStockListClicked() {
        _addToStockList.value = true
    }

    fun addToStockListCompleted() {
        _addToStockList.value = null
    }

    fun addSelectedStockToStockList() {
        val userStock = UserStocksDataModel(_selectedStock.value!!.stockSymbol, _selectedStock.value!!.stockName, 1000)
        coroutineScope.launch {
            stocksRepository.insertStockToUserStocks(userStock)
        }
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