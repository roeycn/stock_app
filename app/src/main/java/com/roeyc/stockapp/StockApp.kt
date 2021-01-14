package com.roeyc.stockapp

import android.app.Application
import android.util.Log
import timber.log.Timber
import timber.log.Timber.DebugTree


class StockApp : Application() {

    fun MyApp() {
        // this method fires only once per application start.
       // getApplicationContext returns null here
        Log.i("main", "Constructor fired")
    }

    override fun onCreate() {
        super.onCreate()

        // this method fires once as well as constructor
        // but also application has context here

        Log.i("main", "onCreate fired");

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

    }

}