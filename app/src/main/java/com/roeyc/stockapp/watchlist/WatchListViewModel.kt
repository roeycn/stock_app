package com.roeyc.stockapp.watchlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roeyc.stockapp.BaseViewModel
import com.roeyc.stockapp.R
import com.roeyc.stockapp.database.getDatabase
import com.roeyc.stockapp.dialogs.AppDialogItem
import com.roeyc.stockapp.domain.UserStocksDataModel
import com.roeyc.stockapp.repository.StocksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class WatchListViewModel(application: Application) : BaseViewModel(application) {

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

    fun showPleaseLoginDialog() {
        Log.i("WatchListViewModel", "show Please Login Dialog")
        showDialogParamsLiveData.value  = AppDialogItem.Builder()
            .setTitleText(getString(R.string.dialog_info_please_login_title))
            .setMessageText(getString(R.string.dialog_info_please_login_message_text))
            .setPositiveButtonText(getString(R.string.dialog_positive_button_text_ok))
            .setStyle(AppDialogItem.DialogStyle.InfoDialog)
            .build()
          //  messageText =  getString(R.string.please_login),
          //  positiveText = R.string.global_ok.toString(),
        //    titleText = "titleText",
        //    messageText = "messageText",
        //    positiveText = "positiveText", isCancelable = true,
        //    dialogStyle = AppDialogItem.DialogStyle.InfoDialog
    }

    override fun onDialogPositiveClicked(requestCode: Int): Boolean {
        when (requestCode) {
            DIALOG_REQUEST_CODE_1 -> {

            }
            DIALOG_REQUEST_CODE_2 -> {

            }
            DIALOG_REQUEST_CODE_3 -> {

            }
            else -> {
                return super.onDialogPositiveClicked(requestCode)
            }
        }
        return super.onDialogPositiveClicked(requestCode)
    }

    override fun onDialogNegativeClicked(requestCode: Int): Boolean {
        return super.onDialogNegativeClicked(requestCode)
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
