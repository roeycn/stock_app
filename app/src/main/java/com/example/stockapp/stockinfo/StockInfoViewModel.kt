package com.example.stockapp.stockinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.getDatabase
import com.example.stockapp.repository.StocksRepository
import com.example.stockapp.stock.StockApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StockInfoViewModel(application: Application) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<StockApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<StockApiStatus>
        get() = _status

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = SupervisorJob()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Create the database singleton.
    private val database = getDatabase(application)
    // Create the repository
    private val stocksRepository = StocksRepository(database)

    init {
        //getStockData()
        coroutineScope.launch {
            stocksRepository.refreshStocks()
        }
    }

    /**
     * Factory for constructing StockInfoViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockInfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StockInfoViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}