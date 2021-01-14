package com.roeyc.stockapp.database

import android.os.Parcel
import android.os.Parcelable

data class Cars (

    var company: String,
    val price: String ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(company)
        parcel.writeString(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cars> {
        override fun createFromParcel(parcel: Parcel): Cars {
            return Cars(parcel)
        }

        override fun newArray(size: Int): Array<Cars?> {
            return arrayOfNulls(size)
        }
    }
}