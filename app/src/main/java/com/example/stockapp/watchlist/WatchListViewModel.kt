package com.example.stockapp.watchlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.getDatabase
import com.example.stockapp.domain.UserStocksDataModel
import com.example.stockapp.repository.StocksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class WatchListViewModel(application: Application) : ViewModel() {

    private val _mSendYourPortfolio = MutableLiveData<Boolean>();
    val mSendYourPortfolio: LiveData<Boolean>
        get() = _mSendYourPortfolio

    private val _navigateToSearch = MutableLiveData<Boolean>();
    val navigateToSearch: LiveData<Boolean>
        get() = _navigateToSearch

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

  //  lateinit var userStocks: LiveData<List<UserStocksDataModel>>

    init {

    }

    val userStocks = stocksRepository.userStocks


//    fun getUserStocks(): LiveData<List<UserStocksDataModel>> {
//        return stocksRepository.userStocks
//    }

    fun sendYourPortfolioClicked() {
        _mSendYourPortfolio.value = true
    }

    fun navigateSearchFragment() {
        _navigateToSearch.value = true
    }

    fun navigateSearchFragmentComplete() {
        _navigateToSearch.value = null
    }

    fun setStartingStocksPrice(mUserStocks: MutableList<UserStocksDataModel>): MutableList<UserStocksDataModel> {
        mUserStocks.let {
            for (stock in it) {
                stock.value = 1444
            }
        }
        return mUserStocks
    }

    fun calculateTotalValue(userStocks: List<UserStocksDataModel>?): Int {

        var total: Int = 0
        for (stockCost in userStocks.orEmpty()) {
            total += stockCost.value
        }
        return total
    }

    fun removeStockFromUserStocks(userStocksDataModel: UserStocksDataModel) {
       // val userStock = UserStocksDataModel(_selectedStock.value!!.stockSymbol, _selectedStock.value!!.stockName, 1000)
        coroutineScope.launch {
            stocksRepository.removeStockFromUserStocks(userStocksDataModel)
        }
    }
    /**
     * Factory for constructing StockViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WatchListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WatchListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
