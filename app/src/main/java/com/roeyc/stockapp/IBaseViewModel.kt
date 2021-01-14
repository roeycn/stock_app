package com.roeyc.stockapp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import com.roeyc.stockapp.dialogs.AppDialogItem

interface IBaseViewModel {

    val showDialogParamsLiveData: LiveData<AppDialogItem>
    val hideDialogLiveData: LiveData<Unit>
    val showLoadingDialogLiveData: LiveData<Boolean>
    val hideSpecificDialogLiveData: LiveData<String>

    fun onDialogPositiveClicked(requestCode: Int): Boolean = false
    fun onDialogNegativeClicked(requestCode: Int): Boolean = false
    fun observeShowDialogLiveData(lifecycle: Lifecycle, observer: (AppDialogItem) -> Unit)
    fun observeShowLoadingDialogLiveData(lifecycle: Lifecycle, observer: (Boolean) -> Unit)

}