package com.roeyc.stockapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.roeyc.stockapp.common.SingleLiveEvent
import com.roeyc.stockapp.dialogs.AppDialogItem

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel  {

    override val showDialogParamsLiveData = SingleLiveEvent<AppDialogItem>()

    override val hideDialogLiveData = MutableLiveData<Unit>()

    override val showLoadingDialogLiveData = MutableLiveData<Boolean>()

    override val hideSpecificDialogLiveData = MutableLiveData<String>()


    override fun observeShowDialogLiveData(lifecycle: Lifecycle,
        observer: (AppDialogItem) -> Unit) {
        showDialogParamsLiveData.observe({lifecycle}) {
            it.let(observer)
        }
    }

    override fun observeShowLoadingDialogLiveData(lifecycle: Lifecycle,
        observer: (Boolean) -> Unit ) {
       showLoadingDialogLiveData.observe({lifecycle}) {
           it.let(observer)
       }
    }

    protected fun getApplicationContext(): Context {
        return getApplication<StockApp>()
    }

    protected fun getString(stringId: Int): String {
        return getApplicationContext().getString(stringId)
    }

    companion object {
        const val DIALOG_REQUEST_CODE_1 = 201
        const val DIALOG_REQUEST_CODE_2 = 202
        const val DIALOG_REQUEST_CODE_3 = 203
    }
}