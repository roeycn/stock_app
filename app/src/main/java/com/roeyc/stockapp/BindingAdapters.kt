package com.roeyc.stockapp

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.roeyc.stockapp.stockinfo.StockInfoApiStatus

/**
 * This binding adapter displays the [MarsApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */


/**
 * This binding adapter displays the [StockInfoApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */

@BindingAdapter("stockInfoApiStatus")
fun bindStatus(statusImageView: ImageView, status: StockInfoApiStatus?) {
    when (status) {
        StockInfoApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        StockInfoApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        StockInfoApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("app:changeDimensionObserver")
fun changeViewDimensions(view: View, helper: ViewDimensionChangeHelper) {
    helper.setOnDimensionChangedListener({ width: Int, height: Int ->
        view.layoutParams.height = height
        view.layoutParams.width = width
    })
}