package com.example.qaapp.stock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qaapp.mars.MarsApiStatus
import com.example.qaapp.network.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

enum class StockApiStatus { LOADING, ERROR, DONE }

class StockViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<StockApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<StockApiStatus>
        get() = _status

    private val _response = MutableLiveData<StockDailyProperty>()
    val response: LiveData<StockDailyProperty>
        get() = _response

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getStockData()
    }

    private fun getStockData() {
        coroutineScope.launch {
             var responseDataDeferred = StockApi.retrofitService.getStockData()
            try {
                _status.value = StockApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val result = responseDataDeferred.await()
                _status.value = StockApiStatus.DONE
                _response.value = result
            } catch (e: Exception) {
                _status.value = StockApiStatus.ERROR
                _response.value = null
            }
        }
    }

}
