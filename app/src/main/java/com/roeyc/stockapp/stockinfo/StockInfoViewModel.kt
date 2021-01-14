package com.roeyc.stockapp.stockinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roeyc.stockapp.ViewDimensionChangeHelper
import com.roeyc.stockapp.database.getDatabase
import com.roeyc.stockapp.domain.StockDataModel
import com.roeyc.stockapp.domain.UserStocksDataModel
import com.roeyc.stockapp.repository.StocksRepository
import com.roeyc.stockapp.search.SearchResultDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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

   var userStocks : LiveData<List<UserStocksDataModel>>

    private var viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val stocksRepository = StocksRepository(database)

    init {
        _selectedStock.value = stockData

        _status.value = StockInfoApiStatus.LOADING
        coroutineScope.launch {
            stocksRepository.refreshStockData(stockData.stockSymbol);
        }
        userStocks = stocksRepository.userStocks
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
        val userStock = UserStocksDataModel(_selectedStock.value!!.stockSymbol, _selectedStock.value!!.stockName, ss.value!!.price, 1000)
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