package com.roeyc.stockapp.stocksList

import android.util.Log
import java.util.HashMap

class Stocks private constructor() {
    var stocks : HashMap<String, String>? = null

    init {
        Log.i("file", "Singleton class invoked.");
    }

//    override fun toString(): String {
//        return "User [userName=$stocks]"
//    }

    companion object {
        private var stocksInstance: Stocks? = null // the only instance of the class
        val instance: Stocks?
            get() {
                if (stocksInstance == null) {
                    stocksInstance = Stocks()
                }
                return stocksInstance
            }
    }
}
