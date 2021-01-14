package com.roeyc.stockapp.firebase

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class FbUserStockData(
    @PropertyName("description") var description: String
) {
    constructor() : this( "")
}