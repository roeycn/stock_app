package com.example.stockapp.search

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.getDatabase
import com.example.stockapp.repository.StocksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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

        fun readFileAsLinesUsingBufferedReader(context: Context?): HashMap<String, String> {
            var list: List<String>

            var nasdaqlisted: String = "nasdaqlisted.txt"
            var nasdaqtraded: String = "nasdaqtraded.txt"

            var file: String = nasdaqtraded
            list = context!!.applicationContext.assets.open(file)?.bufferedReader()!!.readLines()

            val hashMap: HashMap<String, String> = HashMap() //define empty hashmap

            if (file.equals(nasdaqlisted)) {
                for (row in list) {
                    val stockSymbol: String = row.substringBefore('|')
                    val stockAfterSymbol: String = row.substringAfter('|')
                    val stockName: String = stockAfterSymbol.substringBefore('.')
                    hashMap.put(stockSymbol, stockName)
                }
            } else if (file.equals(nasdaqtraded)) {
                for (row in list) {
                    val stockAfterSymbol: String = row.substringAfter('|')
                    val stockSymbol: String = stockAfterSymbol.substringBefore('|')

                    val stockAfterSecondSymbol: String = stockAfterSymbol.substringAfter('|')
                    val stockName: String = stockAfterSecondSymbol.substringBefore('.')
                    hashMap.put(stockSymbol, stockName)
                }
            }
            return hashMap
        }

    }

