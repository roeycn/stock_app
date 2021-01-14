package com.roeyc.stockapp.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roeyc.stockapp.database.getDatabase
import com.roeyc.stockapp.repository.StocksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*

class SearchViewModel(application: Application) : ViewModel() {

    private val _navigateToStockInfoFragment = MutableLiveData<SearchResultDao>()
    val navigateToStockInfoFragment: LiveData<SearchResultDao>
        get() = _navigateToStockInfoFragment


    private var viewModelJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val stocksRepository = StocksRepository(database)

    init {
        //getStockData()
       // coroutineScope.launch {
       //     stocksRepository.refreshStockData("IBM")
      //  }

        // get latest search results
    }

    val stocklist = stocksRepository.stocks


        fun navigateToStockInfo(stockData: SearchResultDao) {
            _navigateToStockInfoFragment.value = stockData
        }

        fun navigateToStockInfoComplete() {
            _navigateToStockInfoFragment.value = null
        }

        /**
         * Factory for constructing StockViewModel with parameter
         */
        class Factory(val app: Application) : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SearchViewModel(app) as T
                }
                throw IllegalArgumentException("Unable to construct viewmodel")
            }
        }

    }

