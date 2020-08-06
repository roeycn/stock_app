package com.example.stockapp.stock

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.getDatabase
import com.example.stockapp.network.*
import com.example.stockapp.repository.StocksRepository
import kotlinx.coroutines.*
import retrofit2.await
import java.lang.Exception

enum class StockApiStatus { LOADING, ERROR, DONE }

class StockViewModel(application: Application) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<StockApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<StockApiStatus>
        get() = _status

    private val _response = MutableLiveData<StockDailyProperty>()
    val response: LiveData<StockDailyProperty>
        get() = _response

    private val _searchResponse = MutableLiveData<StockSearchProperty>()
    val searchResponse: LiveData<StockSearchProperty>
        get() = _searchResponse

    private val _searchButtonClicked = MutableLiveData<Boolean>()
    val searchButtonClicked: LiveData<Boolean>
        get() = _searchButtonClicked


    // via database flow
    private val _stockLastPriceResponse = MutableLiveData<StockSearchProperty>()
    val stockLastPriceResponse: LiveData<StockSearchProperty>
        get() = _stockLastPriceResponse


//    private val _editTextData = MutableLiveData<String>()
//    val editTextData: LiveData<String>
//        get() = _editTextData


    val editTextData = MutableLiveData<String>()

    fun setText(value: String) {
        editTextData.value = "ABC"
    }

    fun getText(): MutableLiveData<String> {
        return editTextData
    }

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
            stocksRepository.refreshStockData("IBM")
        }
    }

    val stocklist = stocksRepository.stocks

    fun startSearch() {
        _searchButtonClicked.value = true
    }

    fun stopSearch() {
        _searchButtonClicked.value = null
    }


//    fun setEditTextData(data: String) {
//        _editTextData.value = data
//    }

//    fun editTextChanged(newText: String) {
//        if (newText == _editTextData.value) {
//            return
//        }
//    }

//    @InverseMethod("convert")
//    fun getText() {
//        _editTextData = newText.toString()
//    }

    private fun getStockData() {
        coroutineScope.launch {
             var responseDataDeferred = StockApi.retrofitService.getStockTimeSeriesIntraday()
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

     fun getSearchResults(keywords: String, apikey: String) {
        coroutineScope.launch {
            var searchResponse = StockApi.retrofitService.getSearchResults(keywords, apikey)
            try {
                // this will run on a thread managed by Retrofit
                val result = searchResponse.await()    // change await import from deffered
                _searchResponse.value = result
            } catch (e: Exception) {
                _searchResponse.value = null
            }
        }
    }

//    fun getStockLastPrice() {
//        coroutineScope.launch {
//            var stockLastPriceResponse = StockApi.retrofitService.getStockLastPrice()
//            try {
//                // this will run on a thread managed by Retrofit
//                val result = stockLastPriceResponse.await()
//                _stockLastPriceResponse.value = result.
//            } catch (e: Exception) {
//                _stockLastPriceResponse.value = null
//            }
//        }
//    }


    /**
     * Factory for constructing StockViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StockViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
