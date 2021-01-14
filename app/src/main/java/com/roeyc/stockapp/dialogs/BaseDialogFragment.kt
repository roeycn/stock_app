package com.roeyc.stockapp.dialogs

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.roeyc.stockapp.watchlist.WatchListFragment
import timber.log.Timber


open class BaseDialogFragment : DialogFragment() {

    private val TAG = BaseDialogFragment::class.java.simpleName

    protected var mIsFragmentPaused = true
    protected var isLandscape = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause")
        mIsFragmentPaused = true
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
        mIsFragmentPaused = false
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart")
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        super.onDestroy()
    }


}