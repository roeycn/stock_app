package com.example.qaapp.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.qaapp.database.Cars

class DetailViewModel(cars: Cars, app: Application) : AndroidViewModel(app) {
    private val _selectedProperty = MutableLiveData<Cars>()

    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<Cars>
        get() = _selectedProperty

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedProperty.value = cars
    }

    val displayCarPrice = Transformations.map(selectedProperty) {
        app.applicationContext.getString(
            it.price.toInt()
        )
    }



}