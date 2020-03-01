package com.example.qaapp.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qaapp.database.Cars

class OverviewModel : ViewModel() {

//    enum class Numbers { ONE, TWO, THREE }
//
//    val myArray = arrayOf<String>("Abu","Praveen","Sathya","Yogesh","Ram")


    private val _mCarCompany = MutableLiveData<List<Cars>>();

    val mCarCompany: LiveData<List<Cars>>
    get() = _mCarCompany

    fun setCarCompany(list: List<Cars>) {
        _mCarCompany.value = list
    }


}