package com.example.qaapp.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qaapp.database.Cars

class OverviewModel : ViewModel() {

    private val _mCarCompany = MutableLiveData<List<Cars>>();
    val mCarCompany: LiveData<List<Cars>>
    get() = _mCarCompany

    private val _navigateToSelectedProperty = MutableLiveData<Cars>()
    val navigateToSelectedProperty : LiveData<Cars>
    get() = _navigateToSelectedProperty

    private val _navigateToMars = MutableLiveData<Boolean>()
    val navigateToMars : LiveData<Boolean>
    get() = _navigateToMars


    fun setCarCompany(list: List<Cars>) {
        _mCarCompany.value = list
    }

    fun displayDetails(cars: Cars) {
        _navigateToSelectedProperty.value = cars
    }

    fun displayDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    fun navigateMarsFragment() {
        _navigateToMars.value = true
    }

    fun navigateMarsFragmentComplete() {
        _navigateToMars.value = null
    }





}