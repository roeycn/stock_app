package com.roeyc.stockapp.overview

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.roeyc.stockapp.database.Cars
import java.util.HashMap

class OverviewModel : ViewModel() {

    private val _navigateToSelectedProperty = MutableLiveData<Cars>()
    val navigateToSelectedProperty : LiveData<Cars>
    get() = _navigateToSelectedProperty

    private val _navigateToSearch = MutableLiveData<Boolean>()
    val navigateToSearch : LiveData<Boolean>
        get() = _navigateToSearch

    private val _navigateToWatchList = MutableLiveData<Boolean>()
    val navigateToWatchList : LiveData<Boolean>
        get() = _navigateToWatchList

    private val _navigateToTopStocks = MutableLiveData<Boolean>()
    val navigateToTopStocks : LiveData<Boolean>
        get() = _navigateToTopStocks

    private val _navigateToBearOrBull = MutableLiveData<Boolean>()
    val navigateToBearOrBull : LiveData<Boolean>
        get() = _navigateToBearOrBull

    fun displayDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    fun navigateSearchFragment() {
        _navigateToSearch.value = true
    }

    fun navigateSearchFragmentComplete() {
        _navigateToSearch.value = null
    }

    fun navigateWatchListFragment() {
        _navigateToWatchList.value = true
    }

    fun navigateWatchListFragmentComplete() {
        _navigateToWatchList.value = null
    }

    fun navigateToTopStocksFragment() {
        _navigateToTopStocks.value = true
    }

    fun navigateToTopStocksFragmentComplete() {
        _navigateToTopStocks.value = null
    }

    fun navigateToBearOrBullFragment() {
        _navigateToBearOrBull.value = true
    }

    fun navigateToBearOrBullFragmentComplete() {
        _navigateToBearOrBull.value = null
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