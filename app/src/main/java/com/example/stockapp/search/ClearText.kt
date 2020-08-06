package com.example.stockapp.search

import android.view.MotionEvent
import android.widget.AutoCompleteTextView

// https://medium.com/@dimabatyuk/adding-clear-button-to-edittext-9655e9dbb721
// it takes callback function as argument which is called when user clicks to right drawable.
fun AutoCompleteTextView.onRightDrawableClicked(onClicked: (view: AutoCompleteTextView) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is AutoCompleteTextView) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}