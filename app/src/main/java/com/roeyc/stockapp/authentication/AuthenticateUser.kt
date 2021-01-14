package com.roeyc.stockapp.authentication

import android.app.Application
import android.content.Context

class AuthenticateUser : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        private var mContext: Context? = null
        val context: Context?
            get() = mContext
    }
}