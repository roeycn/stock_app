package com.example.stockapp.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.Cars
import java.lang.IllegalArgumentException

class DetailViewModelFactory (
    private val cars: Cars,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(cars, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
